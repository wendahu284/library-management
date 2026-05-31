package com.library.mapper;

import com.library.entity.Book;
import com.library.vo.BookVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 图书 Mapper 接口
 */
@Mapper
public interface BookMapper {

    /** 根据ID查询 */
    Book selectById(@Param("id") Long id);

    /** 条件分页查询图书（关联分类名称） */
    List<BookVO> selectPage(@Param("keyword") String keyword,
                            @Param("categoryId") Long categoryId,
                            @Param("offset") Integer offset,
                            @Param("size") Integer size);

    /** 条件统计图书总数 */
    Long countByCondition(@Param("keyword") String keyword,
                          @Param("categoryId") Long categoryId);

    /** 插入图书 */
    int insert(Book book);

    /** 更新图书 */
    int updateById(Book book);

    /** 删除图书 */
    int deleteById(@Param("id") Long id);

    /** 减少可借数量 */
    int decreaseAvailable(@Param("id") Long id);

    /** 增加可借数量 */
    int increaseAvailable(@Param("id") Long id);
}
