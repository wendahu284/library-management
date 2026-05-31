package com.library.mapper;

import com.library.entity.SystemConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统参数 Mapper
 */
@Mapper
public interface SystemConfigMapper {

    /** 查询所有配置 */
    List<SystemConfig> selectAll();

    /** 根据key查询 */
    SystemConfig selectByKey(@Param("configKey") String configKey);

    /** 根据key更新value */
    int updateByKey(@Param("configKey") String configKey,
                    @Param("configValue") String configValue);

    /** 插入配置 */
    int insert(SystemConfig config);
}
