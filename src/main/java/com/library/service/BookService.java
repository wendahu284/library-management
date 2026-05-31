package com.library.service;

import com.library.common.PageResult;
import com.library.dto.BookQueryDTO;
import com.library.entity.Book;
import com.library.vo.BookVO;

import java.util.List;

/**
 * 图书业务接口
 */
public interface BookService {

    /** 条件分页查询图书 */
    PageResult<BookVO> getPage(BookQueryDTO dto);

    /** 根据ID查询 */
    Book getById(Long id);

    /** 新增图书 */
    void add(Book book);

    /** 更新图书 */
    void update(Book book);

    /** 删除图书 */
    void delete(Long id);

    /** 热门图书排行 */
    List<BookVO> getPopularBooks(int limit);
}
