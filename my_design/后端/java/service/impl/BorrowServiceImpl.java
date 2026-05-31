package com.library.service.impl;

import com.library.common.PageResult;
import com.library.dto.BorrowQueryDTO;
import com.library.entity.Book;
import com.library.entity.BorrowRecord;
import com.library.exception.BusinessException;
import com.library.mapper.BookMapper;
import com.library.mapper.BorrowRecordMapper;
import com.library.service.BorrowService;
import com.library.service.SystemConfigService;
import com.library.vo.BorrowVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 借阅业务实现类
 */
@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRecordMapper borrowRecordMapper;
    private final BookMapper bookMapper;
    private final SystemConfigService systemConfigService;

    @Override
    @Transactional
    public void borrowBook(Long userId, Long bookId) {
        // 1. 检查图书是否存在且可借
        Book book = bookMapper.selectById(bookId);
        if (book == null) {
            throw new BusinessException(404, "图书不存在");
        }
        if (book.getAvailableQuantity() == null || book.getAvailableQuantity() <= 0) {
            throw new BusinessException(400, "该图书已全部借出，暂时无法借阅");
        }

        // 2. 检查用户当前借阅数量是否已达上限
        int currentCount = borrowRecordMapper.countActiveByUserId(userId);
        int maxBorrowCount = systemConfigService.getConfigInt("library.borrow.max-count", 5);
        if (currentCount >= maxBorrowCount) {
            throw new BusinessException(400,
                    "您当前借阅数量已达上限（" + maxBorrowCount + "本），请先归还部分图书");
        }

        // 3. 检查是否已借过该图书且未归还
        int duplicate = borrowRecordMapper.countByUserIdAndBookId(userId, bookId);
        if (duplicate > 0) {
            throw new BusinessException(400, "您已借阅过该图书且尚未归还");
        }

        // 4. 创建借阅记录
        BorrowRecord record = new BorrowRecord();
        record.setUserId(userId);
        record.setBookId(bookId);
        record.setBorrowTime(LocalDateTime.now());
        record.setDueTime(LocalDateTime.now().plusDays(systemConfigService.getConfigInt("library.borrow.duration-days", 30)));
        record.setStatus(0); // 借阅中

        borrowRecordMapper.insert(record);

        // 5. 减少图书可借数量
        bookMapper.decreaseAvailable(bookId);
    }

    @Override
    @Transactional
    public void returnBook(Long recordId, Long userId) {
        // 1. 查询借阅记录
        BorrowRecord record = borrowRecordMapper.selectById(recordId);
        if (record == null) {
            throw new BusinessException(404, "借阅记录不存在");
        }

        // 2. 校验记录归属
        if (!record.getUserId().equals(userId)) {
            throw new BusinessException(403, "该借阅记录不属于您，无法操作");
        }

        // 3. 校验记录状态
        if (record.getStatus() == 1) {
            throw new BusinessException(400, "该图书已经归还过了");
        }
        if (record.getStatus() == 3) {
            throw new BusinessException(400, "已提交归还申请，请等待管理员审批");
        }

        // 4. 提交归还申请（状态改为待审批）
        borrowRecordMapper.updateStatus(recordId, 3);
    }

    @Override
    @Transactional
    public void confirmReturn(Long recordId) {
        BorrowRecord record = borrowRecordMapper.selectById(recordId);
        if (record == null) {
            throw new BusinessException(404, "借阅记录不存在");
        }
        if (record.getStatus() != 3) {
            throw new BusinessException(400, "该记录不是待审批状态，请使用审批流程");
        }

        executeReturn(record);
    }

    @Override
    @Transactional
    public void approveReturn(Long recordId) {
        BorrowRecord record = borrowRecordMapper.selectById(recordId);
        if (record == null) {
            throw new BusinessException(404, "借阅记录不存在");
        }
        if (record.getStatus() != 3) {
            throw new BusinessException(400, "该记录不是待审批状态");
        }
        executeReturn(record);
    }

    @Override
    @Transactional
    public void rejectReturn(Long recordId) {
        BorrowRecord record = borrowRecordMapper.selectById(recordId);
        if (record == null) {
            throw new BusinessException(404, "借阅记录不存在");
        }
        if (record.getStatus() != 3) {
            throw new BusinessException(400, "该记录不是待审批状态");
        }
        // 恢复原状态：如果已超过应还时间则恢复为逾期(2)，否则恢复为借阅中(0)
        int originalStatus = LocalDateTime.now().isAfter(record.getDueTime()) ? 2 : 0;
        borrowRecordMapper.updateStatus(recordId, originalStatus);
    }

    /**
     * 执行归还操作（提取为公共方法）
     */
    private void executeReturn(BorrowRecord record) {
        LocalDateTime now = LocalDateTime.now();

        // 计算逾期天数
        int overdueDays = 0;
        if (now.isAfter(record.getDueTime())) {
            overdueDays = (int) ChronoUnit.DAYS.between(record.getDueTime(), now);
        }

        // 更新借阅记录
        record.setReturnTime(now);
        record.setOverdueDays(overdueDays);
        record.setStatus(1); // 已归还
        borrowRecordMapper.updateReturnInfo(record);

        // 恢复图书可借数量
        bookMapper.increaseAvailable(record.getBookId());
    }

    @Override
    public List<BorrowVO> getMyBorrows(Long userId) {
        // 查询前先更新逾期状态
        borrowRecordMapper.updateOverdueStatus();
        return borrowRecordMapper.selectByUserId(userId);
    }

    @Override
    public PageResult<BorrowVO> getAllPage(BorrowQueryDTO dto) {
        // 查询前先更新逾期状态
        borrowRecordMapper.updateOverdueStatus();

        int offset = (dto.getPageNum() - 1) * dto.getPageSize();
        List<BorrowVO> records = borrowRecordMapper.selectAllPage(
                dto.getStatus(), offset, dto.getPageSize());
        Long total = borrowRecordMapper.countAll(dto.getStatus());

        return PageResult.of(total, dto.getPageNum(), dto.getPageSize(), records);
    }
}
