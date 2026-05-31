package com.library.service;

import com.library.common.PageResult;
import com.library.dto.BorrowQueryDTO;
import com.library.vo.BorrowVO;

import java.util.List;

/**
 * 借阅业务接口
 */
public interface BorrowService {

    /** 借阅图书 */
    void borrowBook(Long userId, Long bookId);

    /** 归还图书（读者端 - 提交归还申请，status→3） */
    void returnBook(Long recordId, Long userId);

    /** 管理员确认归还（统一走审批流程，仅处理status=3） */
    void confirmReturn(Long recordId);

    /** 管理员批准归还申请 */
    void approveReturn(Long recordId);

    /** 管理员拒绝归还申请 */
    void rejectReturn(Long recordId);

    /** 查询当前用户借阅记录 */
    List<BorrowVO> getMyBorrows(Long userId);

    /** 管理员分页查询所有借阅记录 */
    PageResult<BorrowVO> getAllPage(BorrowQueryDTO dto);
}
