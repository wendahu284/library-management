package com.library.controller;

import com.library.common.Result;
import com.library.entity.Announcement;
import com.library.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告公开控制器（学生端）
 */
@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    /** 获取生效公告（无需登录） */
    @GetMapping
    public Result<List<Announcement>> list() {
        List<Announcement> list = announcementService.getActiveList();
        return Result.success(list);
    }
}
