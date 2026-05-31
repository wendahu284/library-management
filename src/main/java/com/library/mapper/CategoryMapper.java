package com.library.mapper;

import com.library.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 图书分类 Mapper 接口
 */
@Mapper
public interface CategoryMapper {

    /** 查询全部分类 */
    List<Category> selectAll();

    /** 根据ID查询 */
    Category selectById(@Param("id") Long id);

    /** 根据名称查询 */
    Category selectByName(@Param("name") String name);

    /** 插入分类 */
    int insert(Category category);

    /** 更新分类 */
    int updateById(Category category);

    /** 删除分类 */
    int deleteById(@Param("id") Long id);
}
