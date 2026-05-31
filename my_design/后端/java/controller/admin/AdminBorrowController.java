package com.library.controller.admin;

import com.library.annotation.OperationLog;
import com.library.common.PageResult;
import com.library.common.Result;
import com.library.dto.BorrowQueryDTO;
import com.library.service.BorrowService;
import com.library.utils.SessionUtil;
import com.library.vo.BorrowVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员 - 借阅管理控制器
 */
@RestController
@RequestMapping("/api/admin/borrows")
@RequiredArgsConstructor
public class AdminBorrowController {

    private final BorrowService borrowService;

    /** 校验管理员权限 */
    private boolean checkAdmin(HttpServletRequest request) {
        return SessionUtil.isAdmin(request);
    }

    /** 分页查询所有借阅记录 */
    @GetMapping
    public Result<?> list(BorrowQueryDTO dto, HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        PageResult<BorrowVO> page = borrowService.getAllPage(dto);
        return Result.success(page);
    }

    /** 管理员确认归还 */
    @PutMapping("/{id}/return")
    @OperationLog(operation = "修改", target = "借阅", detail = "确认归还")
    public Result<?> confirmReturn(@PathVariable Long id, HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        borrowService.confirmReturn(id);
        return Result.success("归还确认成功");
    }

    /** 管理员批准归还申请 */
    @PutMapping("/{id}/approve")
    @OperationLog(operation = "修改", target = "借阅", detail = "批准归还")
    public Result<?> approveReturn(@PathVariable Long id, HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        borrowService.approveReturn(id);
        return Result.success("归还已批准");
    }

    /** 管理员拒绝归还申请 */
    @PutMapping("/{id}/reject")
    @OperationLog(operation = "修改", target = "借阅", detail = "拒绝归还")
    public Result<?> rejectReturn(@PathVariable Long id, HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        borrowService.rejectReturn(id);
        return Result.success("归还申请已拒绝");
    }
}
