package com.library.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 图书信息实体类
 */
@Data
public class Book {

    /** 图书主键ID */
    private Long id;

    /** ISBN编号 */
    private String isbn;

    /** 图书名称 */
    private String title;

    /** 作者 */
    private String author;

    /** 出版社 */
    private String publisher;

    /** 所属分类ID */
    private Long categoryId;

    /** 图书简介 */
    private String description;

    /** 封面图片URL */
    private String coverUrl;

    /** 馆藏总数量 */
    private Integer totalQuantity;

    /** 当前可借数量 */
    private Integer availableQuantity;

    /** 入库时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
