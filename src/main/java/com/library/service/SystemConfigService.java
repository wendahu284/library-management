package com.library.service;

import com.library.entity.SystemConfig;

import java.util.List;

/**
 * 系统参数业务接口
 */
public interface SystemConfigService {

    /** 获取所有配置 */
    List<SystemConfig> getAll();

    /** 获取单个配置值 */
    String getConfigValue(String key);

    /** 获取整数配置值 */
    int getConfigInt(String key, int defaultValue);

    /** 更新配置 */
    void update(String key, String value);

    /** 刷新缓存 */
    void reloadCache();
}
