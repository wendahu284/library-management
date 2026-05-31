package com.library.service.impl;

import com.library.common.PageResult;
import com.library.dto.BookQueryDTO;
import com.library.entity.Book;
import com.library.exception.BusinessException;
import com.library.mapper.BookMapper;
import com.library.mapper.BorrowRecordMapper;
import com.library.service.BookService;
import com.library.vo.BookVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 图书业务实现类
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final BorrowRecordMapper borrowRecordMapper;

    @Override
    public PageResult<BookVO> getPage(BookQueryDTO dto) {
        int offset = (dto.getPageNum() - 1) * dto.getPageSize();

        // 查询数据列表和总数
        List<BookVO> records = bookMapper.selectPage(
                dto.getKeyword(), dto.getCategoryId(), offset, dto.getPageSize());
        Long total = bookMapper.countByCondition(dto.getKeyword(), dto.getCategoryId());

        return PageResult.of(total, dto.getPageNum(), dto.getPageSize(), records);
    }

    @Override
    public Book getById(Long id) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            throw new BusinessException(404, "图书不存在");
        }
        return book;
    }

    @Override
    @Transactional
    public void add(Book book) {
        // 新增时确保可借数量 = 总数量
        if (book.getAvailableQuantity() == null) {
            book.setAvailableQuantity(book.getTotalQuantity());
        }
        bookMapper.insert(book);
    }

    @Override
    @Transactional
    public void update(Book book) {
        Book existing = bookMapper.selectById(book.getId());
        if (existing == null) {
            throw new BusinessException(404, "图书不存在");
        }
        // 如果修改了馆藏总数，同步调整可借数量
        if (book.getTotalQuantity() != null) {
            int borrowed = existing.getTotalQuantity() - existing.getAvailableQuantity();
            if (book.getTotalQuantity() < borrowed) {
                throw new BusinessException(400, "馆藏总数不能小于已借出数量（" + borrowed + "本）");
            }
            book.setAvailableQuantity(book.getTotalQuantity() - borrowed);
        }
        bookMapper.updateById(book);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Book existing = bookMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "图书不存在");
        }
        bookMapper.deleteById(id);
    }

    @Override
    public List<BookVO> getPopularBooks(int limit) {
        return borrowRecordMapper.selectPopularBooks(limit);
    }
}
