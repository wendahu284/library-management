package com.library.mapper;

import com.library.entity.BorrowRecord;
import com.library.vo.BorrowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 借阅记录 Mapper 接口
 */
@Mapper
public interface BorrowRecordMapper {

    /** 插入借阅记录 */
    int insert(BorrowRecord record);

    /** 根据ID查询 */
    BorrowRecord selectById(@Param("id") Long id);

    /** 更新归还信息 */
    int updateReturnInfo(BorrowRecord record);

    /** 查询用户当前借阅数量 */
    int countActiveByUserId(@Param("userId") Long userId);

    /** 查询用户是否已借过某书且未归还 */
    int countByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);

    /** 查询用户借阅记录（关联图书信息） */
    List<BorrowVO> selectByUserId(@Param("userId") Long userId);

    /** 分页查询所有借阅记录（管理员，关联用户和图书信息） */
    List<BorrowVO> selectAllPage(@Param("status") Integer status,
                                 @Param("offset") Integer offset,
                                 @Param("size") Integer size);

    /** 统计借阅记录总数 */
    Long countAll(@Param("status") Integer status);

    /** 更新逾期状态（将超过应还时间且未归还的记录标记为逾期） */
    int updateOverdueStatus();

    /** 更新单条记录状态 */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /** 统计待审批归还数量 */
    int countPending();

    /** 热门图书排行（按借阅次数降序） */
    List<com.library.vo.BookVO> selectPopularBooks(@Param("limit") int limit);
}
