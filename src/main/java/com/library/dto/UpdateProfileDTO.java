package com.library.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 更新个人信息请求 DTO
 * <p>只允许修改 realName、phone、email，不允许修改 role 和 status</p>
 */
@Data
public class UpdateProfileDTO {

    /** 真实姓名 */
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    /** 联系电话 */
    @NotBlank(message = "联系电话不能为空")
    private String phone;

    /** 电子邮箱 */
    @NotBlank(message = "电子邮箱不能为空")
    private String email;
}
