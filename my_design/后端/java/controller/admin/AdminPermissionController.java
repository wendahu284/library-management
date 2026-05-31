package com.library.controller.admin;

import com.library.common.Result;
import com.library.entity.Permission;
import com.library.service.PermissionService;
import com.library.utils.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员 - 权限管理控制器
 */
@RestController
@RequestMapping("/api/admin/permissions")
@RequiredArgsConstructor
public class AdminPermissionController {

    private final PermissionService permissionService;

    private boolean checkAdmin(HttpServletRequest request) {
        return SessionUtil.isAdmin(request);
    }

    /** 获取权限配置：所有可用权限 + 各角色拥有的权限 */
    @GetMapping
    public Result<?> getPermissions(HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        List<Permission> allPermissions = permissionService.getAllPermissions();

        Map<String, Object> data = new HashMap<>();
        data.put("allPermissions", allPermissions);
        data.put("adminPermissionIds", permissionService.getRolePermissionIds(1));
        data.put("readerPermissionIds", permissionService.getRolePermissionIds(0));

        return Result.success(data);
    }

    /** 更新某角色的权限 */
    @PutMapping("/{role}")
    public Result<?> updateRolePermissions(@PathVariable Integer role,
                                           @RequestBody Map<String, List<Long>> body,
                                           HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        List<Long> permissionIds = body.get("permissionIds");
        permissionService.updateRolePermissions(role, permissionIds);
        return Result.success("权限更新成功");
    }
}
