package com.library.utils;

import com.library.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Session 工具类
 * <p>用于从 HttpSession 中获取当前登录用户信息</p>
 */
public class SessionUtil {

    private static final String SESSION_USER_KEY = "currentUser";

    /** 将用户存入 Session */
    public static void setUser(HttpSession session, User user) {
        session.setAttribute(SESSION_USER_KEY, user);
    }

    /** 从 Session 获取当前用户 */
    public static User getCurrentUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(SESSION_USER_KEY);
    }

    /** 从 Session 获取当前用户ID */
    public static Long getCurrentUserId(HttpServletRequest request) {
        User user = getCurrentUser(request);
        return user != null ? user.getId() : null;
    }

    /** 判断是否已登录 */
    public static boolean isLogin(HttpServletRequest request) {
        return getCurrentUser(request) != null;
    }

    /** 判断是否为管理员 */
    public static boolean isAdmin(HttpServletRequest request) {
        User user = getCurrentUser(request);
        return user != null && user.getRole() == 1;
    }

    /** 清除 Session */
    public static void clear(HttpSession session) {
        session.invalidate();
    }
}
