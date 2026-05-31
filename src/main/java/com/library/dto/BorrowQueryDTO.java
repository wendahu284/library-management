package com.library.dto;

import lombok.Data;

/**
 * 借阅记录查询 DTO
 */
@Data
public class BorrowQueryDTO {

    /** 借阅状态筛选：null=全部 0=借阅中 1=已归还 2=已逾期 */
    private Integer status;

    /** 当前页码（从1开始） */
    private Integer pageNum = 1;

    /** 每页条数 */
    private Integer pageSize = 10;
}
