package com.library.controller;

import com.library.common.Result;
import com.library.service.BorrowService;
import com.library.utils.SessionUtil;
import com.library.vo.BorrowVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 借阅控制器（读者端，需要登录）
 */
@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

    /**
     * 借阅图书
     */
    @PostMapping("/{bookId}")
    public Result<?> borrowBook(@PathVariable Long bookId, HttpServletRequest request) {
        Long userId = SessionUtil.getCurrentUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        borrowService.borrowBook(userId, bookId);
        return Result.success("借阅成功");
    }

    /**
     * 归还图书
     */
    @PostMapping("/return/{recordId}")
    public Result<?> returnBook(@PathVariable Long recordId, HttpServletRequest request) {
        Long userId = SessionUtil.getCurrentUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        borrowService.returnBook(recordId, userId);
        return Result.success("已提交归还申请，等待管理员审批");
    }

    /**
     * 查询当前用户的借阅记录
     */
    @GetMapping("/my")
    public Result<List<BorrowVO>> myBorrows(HttpServletRequest request) {
        Long userId = SessionUtil.getCurrentUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        return Result.success(borrowService.getMyBorrows(userId));
    }
}
