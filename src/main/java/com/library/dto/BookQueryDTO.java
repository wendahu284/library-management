package com.library.dto;

import lombok.Data;

/**
 * 图书查询条件 DTO
 */
@Data
public class BookQueryDTO {

    /** 搜索关键词（匹配书名、作者、ISBN） */
    private String keyword;

    /** 分类ID筛选 */
    private Long categoryId;

    /** 当前页码（从1开始） */
    private Integer pageNum = 1;

    /** 每页条数 */
    private Integer pageSize = 10;
}
