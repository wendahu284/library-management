package com.library.service.impl;

import com.library.common.PageResult;
import com.library.entity.Announcement;
import com.library.exception.BusinessException;
import com.library.mapper.AnnouncementMapper;
import com.library.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 公告业务实现
 */
@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementMapper announcementMapper;

    @Override
    @Transactional
    public void add(Announcement announcement) {
        if (announcement.getActive() == null) {
            announcement.setActive(1);
        }
        announcementMapper.insert(announcement);
    }

    @Override
    @Transactional
    public void update(Announcement announcement) {
        Announcement existing = announcementMapper.selectById(announcement.getId());
        if (existing == null) {
            throw new BusinessException(404, "公告不存在");
        }
        announcementMapper.updateById(announcement);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Announcement existing = announcementMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "公告不存在");
        }
        announcementMapper.deleteById(id);
    }

    @Override
    public PageResult<Announcement> getPage(Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Announcement> records = announcementMapper.selectPage(offset, pageSize);
        Long total = announcementMapper.countAll();
        return PageResult.of(total, pageNum, pageSize, records);
    }

    @Override
    public List<Announcement> getActiveList() {
        return announcementMapper.selectActive(5);
    }
}
