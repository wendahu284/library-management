package com.library.entity;

import lombok.Data;

/**
 * 权限实体
 */
@Data
public class Permission {

    /** 权限主键ID */
    private Long id;

    /** 权限名称 */
    private String name;

    /** 权限标识码 */
    private String code;

    /** 权限描述 */
    private String description;
}
