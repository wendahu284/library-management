package com.library.service.impl;

import com.library.common.PageResult;
import com.library.entity.OperationLog;
import com.library.mapper.OperationLogMapper;
import com.library.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 操作日志业务实现
 */
@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl implements OperationLogService {

    private final OperationLogMapper operationLogMapper;

    @Override
    @Transactional
    public void save(OperationLog log) {
        operationLogMapper.insert(log);
    }

    @Override
    public PageResult<OperationLog> getPage(Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<OperationLog> records = operationLogMapper.selectPage(offset, pageSize);
        Long total = operationLogMapper.countAll();
        return PageResult.of(total, pageNum, pageSize, records);
    }
}
