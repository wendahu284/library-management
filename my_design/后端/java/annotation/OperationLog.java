package com.library.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * <p>标注在Controller方法上，AOP自动记录操作日志</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /** 操作类型：登录/新增/修改/删除 */
    String operation();

    /** 操作目标：图书/读者/分类/借阅/系统 */
    String target();

    /** 操作详情（支持SpEL表达式，如 #book.title） */
    String detail() default "";
}
