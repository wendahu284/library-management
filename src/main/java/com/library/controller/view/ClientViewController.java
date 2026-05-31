package com.library.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 学生端/客户端页面控制器 (v2.0)
 */
@Controller
public class ClientViewController {

    /** 根路径重定向到学生端首页 */
    @GetMapping("/")
    public String root() { return "redirect:/student/"; }

    /** 统一登录页（保留兼容） */
    @GetMapping("/login")
    public String login() { return "login"; }

    /* ========== 新版学生端路由 ========== */

    /** 学生端首页 */
    @GetMapping({"/student", "/student/"})
    public String studentIndex() { return "student/index"; }

    /** 学生端登录 */
    @GetMapping("/student/login.html")
    public String studentLogin() { return "student/login"; }

    /** 学生端注册 */
    @GetMapping("/student/register.html")
    public String studentRegister() { return "student/register"; }

    /** 学生端图书检索 */
    @GetMapping("/student/books")
    public String studentBooks() { return "student/books"; }

    /** 学生端图书详情 */
    @GetMapping("/student/book-detail")
    public String studentBookDetail(@RequestParam Long id, Model model) {
        model.addAttribute("bookId", id);
        return "student/book-detail";
    }

    /** 学生端我的借阅 */
    @GetMapping("/student/my-borrows")
    public String studentMyBorrows() { return "student/my-borrows"; }

    /** 学生端个人中心 */
    @GetMapping("/student/profile")
    public String studentProfile() { return "student/profile"; }

    /* ========== 旧版客户端路由(重定向兼容) ========== */

    @GetMapping({"/client", "/client/"})
    public String clientIndex() { return "redirect:/student/"; }

    @GetMapping("/client/login")
    public String clientLogin() { return "redirect:/student/login.html"; }

    @GetMapping("/client/register")
    public String clientRegister() { return "redirect:/student/register.html"; }

    @GetMapping("/client/book-detail")
    public String clientBookDetail(@RequestParam Long id) {
        return "redirect:/student/book-detail?id=" + id;
    }

    @GetMapping("/client/my-borrows")
    public String clientMyBorrows() { return "redirect:/student/my-borrows"; }

    @GetMapping("/client/profile")
    public String clientProfile() { return "redirect:/student/profile"; }
}
