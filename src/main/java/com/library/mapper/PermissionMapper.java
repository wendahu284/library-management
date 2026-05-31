package com.library.mapper;

import com.library.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限 Mapper
 */
@Mapper
public interface PermissionMapper {

    /** 查询所有权限 */
    List<Permission> selectAll();

    /** 查询某角色的权限 */
    List<Permission> selectByRole(@Param("role") Integer role);

    /** 插入权限 */
    int insert(Permission permission);
}
