package com.library.controller;

import com.library.common.Result;
import com.library.dto.ChangePasswordDTO;
import com.library.dto.LoginDTO;
import com.library.dto.RegisterDTO;
import com.library.dto.UpdateProfileDTO;
import com.library.entity.OperationLog;
import com.library.entity.User;
import com.library.exception.BusinessException;
import com.library.service.OperationLogService;
import com.library.service.UserService;
import com.library.utils.SessionUtil;
import com.library.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 认证控制器（登录、注册、个人信息）
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final OperationLogService operationLogService;

    /**
     * 统一登录接口
     * <p>loginType: admin=管理员登录, reader或不传=读者登录</p>
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody @Valid LoginDTO dto, HttpServletRequest request) {
        try {
            // 1. 验证登录
            User user = userService.login(dto);

            // 2. 将用户信息存入 Session
            SessionUtil.setUser(request.getSession(), user);

            // 3. 记录登录成功日志
            try {
                OperationLog logEntity = new OperationLog();
                logEntity.setOperation("登录");
                logEntity.setTarget("系统");
                logEntity.setDetail("admin".equals(dto.getLoginType()) ? "管理员登录" : "读者登录");
                logEntity.setUserId(user.getId());
                logEntity.setUsername(user.getUsername());
                String ip = request.getHeader("X-Forwarded-For");
                if (ip == null || ip.isEmpty()) {
                    ip = request.getRemoteAddr();
                }
                logEntity.setIp(ip);
                logEntity.setCreateTime(LocalDateTime.now());
                operationLogService.save(logEntity);
            } catch (Exception e) {
                log.warn("记录登录日志失败: {}", e.getMessage());
            }

            // 4. 返回用户信息（不含密码）
            return Result.success("登录成功", userService.toVO(user));

        } catch (BusinessException e) {
            log.warn("登录失败: username={}, reason={}", dto.getUsername(), e.getMessage());
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("登录异常: username={}", dto.getUsername(), e);
            return Result.error(500, "登录失败，服务器内部错误");
        }
    }

    /**
     * 读者注册
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody @Valid RegisterDTO dto) {
        userService.register(dto);
        return Result.success("注册成功，请登录");
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result<?> logout(HttpServletRequest request) {
        SessionUtil.clear(request.getSession());
        return Result.success("已退出登录");
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/me")
    public Result<?> me(HttpServletRequest request) {
        User user = SessionUtil.getCurrentUser(request);
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        return Result.success(userService.toVO(user));
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<?> changePassword(@RequestBody @Valid ChangePasswordDTO dto,
                                     HttpServletRequest request) {
        Long userId = SessionUtil.getCurrentUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        userService.changePassword(userId, dto);
        SessionUtil.clear(request.getSession());
        return Result.success("密码修改成功，请重新登录");
    }

    /**
     * 更新个人信息
     */
    @PutMapping("/profile")
    public Result<?> updateProfile(@RequestBody @Valid UpdateProfileDTO dto, HttpServletRequest request) {
        Long userId = SessionUtil.getCurrentUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        userService.updateProfile(userId, dto);
        return Result.success("个人信息更新成功");
    }
}
