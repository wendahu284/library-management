package com.library.controller.admin;

import com.library.common.Result;
import com.library.entity.SystemConfig;
import com.library.service.SystemConfigService;
import com.library.utils.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理员 - 系统参数控制器
 */
@RestController
@RequestMapping("/api/admin/configs")
@RequiredArgsConstructor
public class AdminConfigController {

    private final SystemConfigService systemConfigService;

    private boolean checkAdmin(HttpServletRequest request) {
        return SessionUtil.isAdmin(request);
    }

    /** 获取所有系统参数 */
    @GetMapping
    public Result<List<SystemConfig>> list(HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        return Result.success(systemConfigService.getAll());
    }

    /** 更新系统参数 */
    @PutMapping
    public Result<?> update(@RequestBody Map<String, String> body, HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        String key = body.get("key");
        String value = body.get("value");
        if (key == null || value == null) {
            return Result.error(400, "参数key和value不能为空");
        }
        systemConfigService.update(key, value);
        return Result.success("参数更新成功");
    }
}
