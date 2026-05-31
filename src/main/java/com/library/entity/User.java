package com.library.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类（区分管理员与读者角色）
 */
@Data
public class User {

    /** 用户主键ID */
    private Long id;

    /** 登录用户名 */
    private String username;

    /** 登录密码（BCrypt加密） */
    private String password;

    /** 真实姓名 */
    private String realName;

    /** 联系电话 */
    private String phone;

    /** 电子邮箱 */
    private String email;

    /** 角色：0=普通读者 1=管理员 */
    private Integer role;

    /** 账号状态：0=已禁用 1=正常 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
