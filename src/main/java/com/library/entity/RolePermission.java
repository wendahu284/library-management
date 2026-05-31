package com.library.entity;

import lombok.Data;

/**
 * 角色-权限关联实体
 */
@Data
public class RolePermission {

    /** 主键ID */
    private Long id;

    /** 角色：0=读者 1=管理员 */
    private Integer role;

    /** 权限ID */
    private Long permissionId;
}
