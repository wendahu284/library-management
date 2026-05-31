package com.library.service.impl;

import com.library.common.PageResult;
import com.library.dto.ChangePasswordDTO;
import com.library.dto.LoginDTO;
import com.library.dto.RegisterDTO;
import com.library.dto.UpdateProfileDTO;
import com.library.entity.User;
import com.library.exception.BusinessException;
import com.library.mapper.UserMapper;
import com.library.service.UserService;
import com.library.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户业务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User login(LoginDTO dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        String loginType = dto.getLoginType();

        // 1. 空值校验（兜底防护）
        if (!StringUtils.hasText(username)) {
            throw new BusinessException(400, "用户名不能为空");
        }
        if (!StringUtils.hasText(password)) {
            throw new BusinessException(400, "密码不能为空");
        }

        // 2. 根据用户名查询用户
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            log.warn("登录失败: 用户名不存在 username={}", username);
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 3. 检查账号状态（null 视为正常）
        Integer status = user.getStatus();
        if (status != null && status == 0) {
            log.warn("登录失败: 账号已禁用 username={}", username);
            throw new BusinessException(403, "该账号已被禁用，请联系管理员");
        }

        // 4. 验证密码（密码字段可能为 null，需防护）
        String storedPassword = user.getPassword();
        if (storedPassword == null) {
            log.error("登录失败: 用户密码字段为空 username={}", username);
            throw new BusinessException(500, "账号数据异常，请联系管理员");
        }
        if (!passwordEncoder.matches(password, storedPassword)) {
            log.warn("登录失败: 密码错误 username={}", username);
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 5. 验证角色权限（role 为 null 时视为读者）
        Integer role = user.getRole();
        int actualRole = (role != null) ? role : 0;

        // 双端登录入口隔离：读者只能从学生端登录，管理员只能从管理端登录
        if ("admin".equals(loginType)) {
            if (actualRole != 1) {
                log.warn("登录失败: 非管理员尝试登录后台 username={}", username);
                throw new BusinessException(403, "非管理员账号，无法登录管理后台");
            }
        } else {
            // loginType 为 "reader" 或不传，视为读者端登录
            if (actualRole == 1) {
                log.warn("登录失败: 管理员尝试从读者端登录 username={}", username);
                throw new BusinessException(403, "管理员账号，请从管理后台登录");
            }
        }

        // 清除密码再返回
        user.setPassword(null);
        log.info("登录成功: username={}, role={}", username, actualRole);
        return user;
    }

    @Override
    @Transactional
    public void register(RegisterDTO dto) {
        // 1. 校验两次密码是否一致
        if (!Objects.equals(dto.getPassword(), dto.getConfirmPassword())) {
            throw new BusinessException(400, "两次输入的密码不一致");
        }

        // 2. 检查用户名是否已存在
        User existing = userMapper.selectByUsername(dto.getUsername());
        if (existing != null) {
            throw new BusinessException(400, "该用户名已被注册");
        }

        // 3. 创建用户
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // BCrypt加密
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setRole(0);   // 默认为读者
        user.setStatus(1); // 默认正常状态

        userMapper.insert(user);
    }

    @Override
    public User getById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        return user;
    }

    @Override
    @Transactional
    public void updateProfile(Long userId, UpdateProfileDTO dto) {
        User existing = userMapper.selectById(userId);
        if (existing == null) {
            throw new BusinessException(404, "用户不存在");
        }
        // 只允许修改 realName、phone、email，不修改 role 和 status
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setRealName(dto.getRealName());
        updateUser.setPhone(dto.getPhone());
        updateUser.setEmail(dto.getEmail());
        userMapper.updateById(updateUser);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordDTO dto) {
        // 1. 校验两次新密码是否一致
        if (!Objects.equals(dto.getNewPassword(), dto.getConfirmPassword())) {
            throw new BusinessException(400, "两次输入的新密码不一致");
        }

        // 2. 获取用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        // 3. 验证原密码
        String storedPassword = user.getPassword();
        if (storedPassword == null) {
            throw new BusinessException(500, "账号数据异常");
        }
        if (!passwordEncoder.matches(dto.getOldPassword(), storedPassword)) {
            throw new BusinessException(400, "原密码错误");
        }

        // 4. 更新密码
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userMapper.updateById(updateUser);
    }

    @Override
    public PageResult<UserVO> getReaderPage(Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<User> readers = userMapper.selectReaderPage(offset, pageSize);
        Long total = userMapper.countReaders();

        List<UserVO> records = readers.stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        return PageResult.of(total, pageNum, pageSize, records);
    }

    @Override
    @Transactional
    public void toggleStatus(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        Integer role = user.getRole();
        if (role != null && role == 1) {
            throw new BusinessException(400, "不能禁用管理员账号");
        }

        Integer currentStatus = user.getStatus();
        int newStatus = (currentStatus != null && currentStatus == 1) ? 0 : 1;

        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setStatus(newStatus);
        userMapper.updateById(updateUser);
    }

    @Override
    public UserVO toVO(User user) {
        if (user == null) return null;
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());
        vo.setRole(user.getRole());
        vo.setStatus(user.getStatus());
        vo.setCreateTime(user.getCreateTime());
        return vo;
    }
}
