package com.library.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统参数实体
 */
@Data
public class SystemConfig {

    /** 主键ID */
    private Long id;

    /** 参数键名 */
    private String configKey;

    /** 参数值 */
    private String configValue;

    /** 参数说明 */
    private String description;

    /** 是否可在线修改：1=可修改 0=只读 */
    private Integer editable;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
