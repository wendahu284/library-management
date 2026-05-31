package com.library.service;

import com.library.common.PageResult;
import com.library.dto.ChangePasswordDTO;
import com.library.dto.LoginDTO;
import com.library.dto.RegisterDTO;
import com.library.dto.UpdateProfileDTO;
import com.library.entity.User;
import com.library.vo.UserVO;

/**
 * 用户业务接口
 */
public interface UserService {

    /** 用户登录 */
    User login(LoginDTO dto);

    /** 读者注册 */
    void register(RegisterDTO dto);

    /** 根据ID查询 */
    User getById(Long id);

    /** 更新个人信息 */
    void updateProfile(Long userId, UpdateProfileDTO dto);

    /** 修改密码 */
    void changePassword(Long userId, ChangePasswordDTO dto);

    /** 分页查询读者列表（管理员） */
    PageResult<UserVO> getReaderPage(Integer pageNum, Integer pageSize);

    /** 切换用户启用/禁用状态 */
    void toggleStatus(Long userId);

    /** 将User实体转为UserVO（去除敏感字段） */
    UserVO toVO(User user);
}
