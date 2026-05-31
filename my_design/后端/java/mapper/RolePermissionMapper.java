package com.library.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色-权限关联 Mapper
 */
@Mapper
public interface RolePermissionMapper {

    /** 查询某角色拥有的权限ID列表 */
    List<Long> selectPermissionIdsByRole(@Param("role") Integer role);

    /** 删除某角色的所有权限 */
    int deleteByRole(@Param("role") Integer role);

    /** 批量插入角色-权限关联 */
    int insertBatch(@Param("role") Integer role,
                    @Param("permissionIds") List<Long> permissionIds);
}
