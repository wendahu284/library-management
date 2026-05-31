package com.library.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 图书借阅记录实体类
 */
@Data
public class BorrowRecord {

    /** 借阅记录主键ID */
    private Long id;

    /** 借阅用户ID */
    private Long userId;

    /** 借阅图书ID */
    private Long bookId;

    /** 借阅时间 */
    private LocalDateTime borrowTime;

    /** 应还时间（借阅时间 + 30天） */
    private LocalDateTime dueTime;

    /** 实际归还时间 */
    private LocalDateTime returnTime;

    /** 逾期天数（归还时自动计算） */
    private Integer overdueDays;

    /** 借阅状态：0=借阅中 1=已归还 2=已逾期 3=待审批归还 */
    private Integer status;
}
