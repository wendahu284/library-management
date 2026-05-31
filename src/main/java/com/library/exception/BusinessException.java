package com.library.exception;

import lombok.Getter;

/**
 * 自定义业务异常
 * <p>用于业务逻辑校验失败时抛出，由全局异常处理器统一捕获</p>
 */
@Getter
public class BusinessException extends RuntimeException {

    /** 异常状态码 */
    private final Integer code;

    /** 构造方法 */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /** 默认500状态码 */
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }
}
