package com.library.service;

import com.library.common.PageResult;
import com.library.entity.OperationLog;

/**
 * 操作日志业务接口
 */
public interface OperationLogService {

    /** 保存操作日志 */
    void save(OperationLog log);

    /** 分页查询 */
    PageResult<OperationLog> getPage(Integer pageNum, Integer pageSize);
}
