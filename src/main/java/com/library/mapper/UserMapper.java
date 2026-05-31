package com.library.mapper;

import com.library.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 Mapper 接口
 */
@Mapper
public interface UserMapper {

    /** 根据用户名查询用户 */
    User selectByUsername(@Param("username") String username);

    /** 根据ID查询用户 */
    User selectById(@Param("id") Long id);

    /** 插入用户 */
    int insert(User user);

    /** 更新用户信息 */
    int updateById(User user);

    /** 分页查询读者列表 */
    List<User> selectReaderPage(@Param("offset") Integer offset, @Param("size") Integer size);

    /** 统计读者总数 */
    Long countReaders();
}
