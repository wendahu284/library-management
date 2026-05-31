package com.library.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 图书信息返回 VO（包含分类名称）
 */
@Data
public class BookVO {

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

    /** 分类名称 */
    private String categoryName;

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

    /** 借阅总次数（热门排行用） */
    private Integer borrowCount;
}
