package com.library.controller.admin;

import com.library.annotation.OperationLog;
import com.library.common.Result;
import com.library.entity.Book;
import com.library.service.BookService;
import com.library.utils.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员 - 图书管理控制器
 */
@RestController
@RequestMapping("/api/admin/books")
@RequiredArgsConstructor
public class AdminBookController {

    private final BookService bookService;

    /** 校验管理员权限 */
    private boolean checkAdmin(HttpServletRequest request) {
        return SessionUtil.isAdmin(request);
    }

    /** 新增图书 */
    @PostMapping
    @OperationLog(operation = "新增", target = "图书", detail = "添加图书")
    public Result<?> add(@RequestBody Book book, HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        bookService.add(book);
        return Result.success("图书添加成功");
    }

    /** 更新图书 */
    @PutMapping("/{id}")
    @OperationLog(operation = "修改", target = "图书", detail = "更新图书")
    public Result<?> update(@PathVariable Long id, @RequestBody Book book,
                            HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        book.setId(id);
        bookService.update(book);
        return Result.success("图书更新成功");
    }

    /** 删除图书 */
    @DeleteMapping("/{id}")
    @OperationLog(operation = "删除", target = "图书", detail = "删除图书")
    public Result<?> delete(@PathVariable Long id, HttpServletRequest request) {
        if (!checkAdmin(request)) return Result.error(403, "无管理员权限");
        bookService.delete(id);
        return Result.success("图书删除成功");
    }
}
