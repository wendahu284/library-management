package com.library.service.impl;

import com.library.entity.Category;
import com.library.exception.BusinessException;
import com.library.mapper.CategoryMapper;
import com.library.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 图书分类业务实现类
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> getAll() {
        return categoryMapper.selectAll();
    }

    @Override
    @Transactional
    public void add(Category category) {
        // 检查分类名称是否已存在
        Category existing = categoryMapper.selectByName(category.getName());
        if (existing != null) {
            throw new BusinessException(400, "该分类名称已存在");
        }
        categoryMapper.insert(category);
    }

    @Override
    @Transactional
    public void update(Category category) {
        // 检查分类是否存在
        Category existing = categoryMapper.selectById(category.getId());
        if (existing == null) {
            throw new BusinessException(404, "分类不存在");
        }

        // 检查新名称是否与其他分类冲突
        Category nameConflict = categoryMapper.selectByName(category.getName());
        if (nameConflict != null && !nameConflict.getId().equals(category.getId())) {
            throw new BusinessException(400, "该分类名称已存在");
        }

        categoryMapper.updateById(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category existing = categoryMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "分类不存在");
        }
        categoryMapper.deleteById(id);
    }
}
