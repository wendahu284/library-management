package com.library.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 公告实体
 */
@Data
public class Announcement {

    /** 公告主键ID */
    private Long id;

    /** 公告标题 */
    private String title;

    /** 公告内容 */
    private String content;

    /** 状态：1=显示 0=隐藏 */
    private Integer active;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
