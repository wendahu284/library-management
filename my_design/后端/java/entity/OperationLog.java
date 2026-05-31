package com.library.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 操作日志实体
 */
@Data
public class OperationLog {

    /** 日志主键ID */
    private Long id;

    /** 操作用户ID */
    private Long userId;

    /** 操作用户名 */
    private String username;

    /** 操作类型：登录/新增/修改/删除 */
    private String operation;

    /** 操作目标 */
    private String target;

    /** 操作详情 */
    private String detail;

    /** 操作IP */
    private String ip;

    /** 创建时间 */
    private LocalDateTime createTime;
}
