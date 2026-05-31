package com.library.common;

import lombok.Data;
import java.util.List;

/**
 * 分页查询统一返回结果
 *
 * @param <T> 列表项数据类型
 */
@Data
public class PageResult<T> {

    /** 总记录数 */
    private Long total;

    /** 当前页码 */
    private Integer pageNum;

    /** 每页条数 */
    private Integer pageSize;

    /** 总页数 */
    private Integer totalPages;

    /** 当前页数据列表 */
    private List<T> records;

    /**
     * 构建分页结果
     */
    public static <T> PageResult<T> of(Long total, Integer pageNum, Integer pageSize, List<T> records) {
        if (pageSize == null || pageSize <= 0) {
            throw new IllegalArgumentException("pageSize must be positive");
        }
        PageResult<T> result = new PageResult<>();
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotalPages((int) Math.ceil((double) total / pageSize));
        result.setRecords(records);
        return result;
    }
}
