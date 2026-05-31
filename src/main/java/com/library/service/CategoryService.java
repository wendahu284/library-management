package com.library.service;

import com.library.entity.Category;
import java.util.List;

/**
 * 图书分类业务接口
 */
public interface CategoryService {

    /** 查询全部分类 */
    List<Category> getAll();

    /** 新增分类 */
    void add(Category category);

    /** 更新分类 */
    void update(Category category);

    /** 删除分类 */
    void delete(Long id);
}
