package com.library.controller;

import com.library.common.Result;
import com.library.dto.BookQueryDTO;
import com.library.entity.Book;
import com.library.service.BookService;
import com.library.vo.BookVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 图书查询控制器（公开接口，无需登录）
 */
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * 条件分页查询图书列表
     */
    @GetMapping
    public Result<com.library.common.PageResult<BookVO>> list(BookQueryDTO dto) {
        return Result.success(bookService.getPage(dto));
    }

    /**
     * 查询图书详情
     */
    @GetMapping("/{id}")
    public Result<Book> detail(@PathVariable Long id) {
        return Result.success(bookService.getById(id));
    }

    /**
     * 热门图书排行（按借阅次数降序）
     */
    @GetMapping("/popular")
    public Result<List<BookVO>> popular(@RequestParam(defaultValue = "6") int limit) {
        if (limit <= 0) {
            limit = 6;
        }
        return Result.success(bookService.getPopularBooks(limit));
    }
}
