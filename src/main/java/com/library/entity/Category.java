package com.library.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 图书分类实体类
 */
@Data
public class Category {

    /** 分类主键ID */
    private Long id;

    /** 分类名称 */
    private String name;

    /** 分类描述 */
    private String description;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
