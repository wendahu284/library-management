package com.library.service;

import com.library.entity.Permission;

import java.util.List;

/**
 * 权限管理业务接口
 */
public interface PermissionService {

    /** 获取全部可用权限列表 */
    List<Permission> getAllPermissions();

    /** 获取某角色的权限ID列表 */
    List<Long> getRolePermissionIds(Integer role);

    /** 更新角色权限 */
    void updateRolePermissions(Integer role, List<Long> permissionIds);
}
