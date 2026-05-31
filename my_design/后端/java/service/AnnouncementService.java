package com.library.service;

import com.library.common.PageResult;
import com.library.entity.Announcement;

import java.util.List;

/**
 * 公告业务接口
 */
public interface AnnouncementService {

    /** 发布公告 */
    void add(Announcement announcement);

    /** 更新公告 */
    void update(Announcement announcement);

    /** 删除公告 */
    void delete(Long id);

    /** 管理员分页查询 */
    PageResult<Announcement> getPage(Integer pageNum, Integer pageSize);

    /** 获取生效公告列表（学生端） */
    List<Announcement> getActiveList();
}
