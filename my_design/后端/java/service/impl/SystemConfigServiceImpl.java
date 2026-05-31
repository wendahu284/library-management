package com.library.service.impl;

import com.library.entity.SystemConfig;
import com.library.exception.BusinessException;
import com.library.mapper.SystemConfigMapper;
import com.library.service.SystemConfigService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统参数业务实现（内存缓存 + DB持久化）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigMapper systemConfigMapper;

    /** 内存缓存：key -> value */
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        reloadCache();
        log.info("系统参数缓存加载完成，共 {} 项", cache.size());
    }

    @Override
    public List<SystemConfig> getAll() {
        return systemConfigMapper.selectAll();
    }

    @Override
    public String getConfigValue(String key) {
        String value = cache.get(key);
        if (value == null) {
            // 缓存未命中，从DB加载
            SystemConfig config = systemConfigMapper.selectByKey(key);
            if (config != null) {
                value = config.getConfigValue();
                cache.put(key, value);
            }
        }
        return value;
    }

    @Override
    public int getConfigInt(String key, int defaultValue) {
        String value = getConfigValue(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    @Transactional
    public void update(String key, String value) {
        SystemConfig config = systemConfigMapper.selectByKey(key);
        if (config == null) {
            throw new BusinessException(404, "参数不存在: " + key);
        }
        if (config.getEditable() != null && config.getEditable() == 0) {
            throw new BusinessException(400, "该参数为系统级参数，不可在线修改");
        }
        systemConfigMapper.updateByKey(key, value);
        // 刷新缓存
        cache.put(key, value);
        log.info("系统参数已更新: {} = {}", key, value);
    }

    @Override
    public void reloadCache() {
        cache.clear();
        List<SystemConfig> all = systemConfigMapper.selectAll();
        for (SystemConfig config : all) {
            cache.put(config.getConfigKey(), config.getConfigValue());
        }
    }
}
