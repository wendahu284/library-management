package com.library.mapper;

import com.library.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作日志 Mapper 接口
 */
@Mapper
public interface OperationLogMapper {

    /** 插入日志 */
    int insert(OperationLog log);

    /** 分页查询 */
    List<OperationLog> selectPage(@Param("offset") Integer offset,
                                  @Param("size") Integer size);

    /** 统计总数 */
    Long countAll();
}
