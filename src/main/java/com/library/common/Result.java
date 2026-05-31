package com.library.common;

import lombok.Data;

/**
 * 全局统一返回结果类
 * <p>所有接口统一使用此类封装返回值</p>
 *
 * @param <T> 响应数据类型
 */
@Data
public class Result<T> {

    /** 状态码：200=成功，其他=失败 */
    private Integer code;

    /** 提示信息 */
    private String message;

    /** 响应数据 */
    private T data;

    private Result() {}

    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ==================== 成功响应 ====================

    /** 成功（无数据） */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    /** 成功（携带数据） */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /** 成功（自定义消息 + 数据） */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    // ==================== 失败响应 ====================

    /** 失败（自定义状态码 + 消息） */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /** 失败（默认500状态码） */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
}
