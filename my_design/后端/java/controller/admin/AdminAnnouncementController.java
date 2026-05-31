package com.library.controller.admin;

import com.library.common.PageResult;
import com.library.common.Result;
import com.library.entity.Announcement;
import com.library.service.AnnouncementService;
import com.library.utils.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员 - 公告管理控制器
 */
@RestController
@RequestMapping("/api/admin/announcements")
@RequiredArgsConstructor
public class AdminAnnouncementController {

    private final AnnouncementService announcementService;

    private boolean checkAdmin(HttpServletRequest request) {
        return SessionUtil.isAdmin(request);
    }

    /** 发布公告 */
    @PostMapping
    public Result<?> add(@RequestBody Announcement announcement, HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        announcementService.add(announcement);
        return Result.success("公告发布成功");
    }

    /** 更新公告 */
    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Announcement announcement,
                            HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        announcement.setId(id);
        announcementService.update(announcement);
        return Result.success("公告更新成功");
    }

    /** 删除公告 */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id, HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        announcementService.delete(id);
        return Result.success("公告删除成功");
    }

    /** 分页查询公告（含隐藏的） */
    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        PageResult<Announcement> page = announcementService.getPage(pageNum, pageSize);
        return Result.success(page);
    }
}
