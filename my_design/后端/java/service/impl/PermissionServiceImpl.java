package com.library.service.impl;

import com.library.entity.Permission;
import com.library.mapper.PermissionMapper;
import com.library.mapper.RolePermissionMapper;
import com.library.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限管理业务实现
 * <p>注：权限系统当前仅为 v2.1 预留，尚未接入 HandlerInterceptor 或 Filter 进行细粒度权限校验。
 * 当前管理端 API 仅通过 checkAdmin()（role=1）做粗粒度鉴权，权限数据存在于数据库但未生效。</p>
 */
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;
    private final RolePermissionMapper rolePermissionMapper;

    @Override
    public List<Permission> getAllPermissions() {
        return permissionMapper.selectAll();
    }

    @Override
    public List<Long> getRolePermissionIds(Integer role) {
        return rolePermissionMapper.selectPermissionIdsByRole(role);
    }

    @Override
    @Transactional
    public void updateRolePermissions(Integer role, List<Long> permissionIds) {
        rolePermissionMapper.deleteByRole(role);
        if (permissionIds != null && !permissionIds.isEmpty()) {
            rolePermissionMapper.insertBatch(role, permissionIds);
        }
    }
}
