package com.library.controller.admin;

import com.library.annotation.OperationLog;
import com.library.common.PageResult;
import com.library.common.Result;
import com.library.service.UserService;
import com.library.utils.SessionUtil;
import com.library.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员 - 用户管理控制器
 */
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    /**
     * 校验管理员权限
     */
    private boolean checkAdmin(HttpServletRequest request) {
        return SessionUtil.isAdmin(request);
    }

    /**
     * 分页查询读者列表
     */
    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          HttpServletRequest request) {
        if (!checkAdmin(request)) {
            return Result.error(403, "无管理员权限");
        }
        PageResult<UserVO> page = userService.getReaderPage(pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 切换用户启用/禁用状态
     */
    @PutMapping("/{id}/status")
    @OperationLog(operation = "修改", target = "读者", detail = "切换读者状态")
    public Result<?> toggleStatus(@PathVariable Long id, HttpServletRequest request) {
        if (!checkAdmin(request)) {
            return Result.error(403, "无管理员权限");
        }
        userService.toggleStatus(id);
        return Result.success("状态更新成功");
    }
}
