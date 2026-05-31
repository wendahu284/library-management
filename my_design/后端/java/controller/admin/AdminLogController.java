package com.library.controller.admin;

import com.library.common.PageResult;
import com.library.common.Result;
import com.library.entity.OperationLog;
import com.library.service.OperationLogService;
import com.library.utils.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员 - 操作日志查询控制器
 */
@RestController
@RequestMapping("/api/admin/logs")
@RequiredArgsConstructor
public class AdminLogController {

    private final OperationLogService operationLogService;

    private boolean checkAdmin(HttpServletRequest request) {
        return SessionUtil.isAdmin(request);
    }

    /** 分页查询操作日志 */
    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "15") Integer pageSize,
                          HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        PageResult<OperationLog> page = operationLogService.getPage(pageNum, pageSize);
        return Result.success(page);
    }
}
