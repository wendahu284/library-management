package com.library.controller.admin;

import com.library.annotation.OperationLog;
import com.library.common.Result;
import com.library.entity.Category;
import com.library.service.CategoryService;
import com.library.utils.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员 - 图书分类管理控制器
 */
@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    /** 校验管理员权限 */
    private boolean checkAdmin(HttpServletRequest request) {
        return SessionUtil.isAdmin(request);
    }

    /** 查询全部分类 */
    @GetMapping
    public Result<List<Category>> list(HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        return Result.success(categoryService.getAll());
    }

    /** 新增分类 */
    @PostMapping
    @OperationLog(operation = "新增", target = "分类", detail = "添加图书分类")
    public Result<?> add(@RequestBody Category category, HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        categoryService.add(category);
        return Result.success("分类添加成功");
    }

    /** 更新分类 */
    @PutMapping("/{id}")
    @OperationLog(operation = "修改", target = "分类", detail = "更新图书分类")
    public Result<?> update(@PathVariable Long id, @RequestBody Category category,
                            HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        category.setId(id);
        categoryService.update(category);
        return Result.success("分类更新成功");
    }

    /** 删除分类 */
    @DeleteMapping("/{id}")
    @OperationLog(operation = "删除", target = "分类", detail = "删除图书分类")
    public Result<?> delete(@PathVariable Long id, HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        categoryService.delete(id);
        return Result.success("分类删除成功");
    }
}
