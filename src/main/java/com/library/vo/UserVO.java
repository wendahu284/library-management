package com.library.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户信息返回 VO（不包含密码等敏感字段）
 */
@Data
public class UserVO {

    /** 用户主键ID */
    private Long id;

    /** 登录用户名 */
    private String username;

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
}
