package com.library.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 借阅记录返回 VO（关联用户名和图书名）
 */
@Data
public class BorrowVO {

    /** 借阅记录主键ID */
    private Long id;

    /** 借阅用户ID */
    private Long userId;

    /** 借阅人姓名 */
    private String userName;

    /** 借阅图书ID */
    private Long bookId;

    /** 图书名称 */
    private String bookTitle;

    /** 图书ISBN */
    private String bookIsbn;

    /** 借阅时间 */
    private LocalDateTime borrowTime;

    /** 应还时间 */
    private LocalDateTime dueTime;

    /** 实际归还时间 */
    private LocalDateTime returnTime;

    /** 逾期天数 */
    private Integer overdueDays;

    /** 借阅状态：0=借阅中 1=已归还 2=已逾期 */
    private Integer status;
}
