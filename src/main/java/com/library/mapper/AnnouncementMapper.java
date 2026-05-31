package com.library.mapper;

import com.library.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公告 Mapper 接口
 */
@Mapper
public interface AnnouncementMapper {

    /** 插入公告 */
    int insert(Announcement announcement);

    /** 根据ID更新公告 */
    int updateById(Announcement announcement);

    /** 根据ID删除公告 */
    int deleteById(@Param("id") Long id);

    /** 根据ID查询 */
    Announcement selectById(@Param("id") Long id);

    /** 分页查询所有公告（管理员） */
    List<Announcement> selectPage(@Param("offset") Integer offset,
                                  @Param("size") Integer size);

    /** 统计总数 */
    Long countAll();

    /** 查询生效中的公告（学生端，按时间倒序） */
    List<Announcement> selectActive(@Param("limit") Integer limit);
}
