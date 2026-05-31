package com.library.aspect;

import com.library.annotation.OperationLog;
import com.library.entity.User;
import com.library.service.OperationLogService;
import com.library.utils.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

/**
 * 操作日志 AOP 切面
 * <p>拦截 @OperationLog 注解的方法，自动记录操作日志</p>
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private final OperationLogService operationLogService;

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        // 执行目标方法
        Object result = joinPoint.proceed();

        // 异步记录日志（失败不影响业务）
        try {
            com.library.entity.OperationLog logEntity = new com.library.entity.OperationLog();
            logEntity.setOperation(operationLog.operation());
            logEntity.setTarget(operationLog.target());

            // 构建详情
            String detail = operationLog.detail();
            if (detail.isEmpty()) {
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                detail = signature.getMethod().getName();
            }
            logEntity.setDetail(detail);

            // 获取当前用户
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                User user = SessionUtil.getCurrentUser(request);
                if (user != null) {
                    logEntity.setUserId(user.getId());
                    logEntity.setUsername(user.getUsername());
                }
                // 获取IP
                String ip = request.getHeader("X-Forwarded-For");
                if (ip == null || ip.isEmpty()) {
                    ip = request.getRemoteAddr();
                }
                logEntity.setIp(ip);
            }

            logEntity.setCreateTime(LocalDateTime.now());
            operationLogService.save(logEntity);
        } catch (Exception e) {
            log.warn("记录操作日志失败: {}", e.getMessage());
        }

        return result;
    }
}
