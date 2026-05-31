package com.library.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

/**
 * 管理员端页面控制器 (v2.0)
 */
@Controller
public class AdminViewController {

    /** 管理员登录页 */
    @GetMapping("/admin/login.html")
    public String loginPage() { return "admin/login"; }

    /** 旧管理员登录 - 重定向到新页面 */
    @GetMapping("/admin/login")
    public String loginRedirect() { return "redirect:/admin/login.html"; }

    /** 仪表盘 */
    @GetMapping({"/admin", "/admin/"})
    public String index() { return "admin/index"; }

    /** 图书管理 */
    @GetMapping("/admin/books")
    public String books() { return "admin/books"; }

    /** 图书表单（新增/编辑） */
    @GetMapping("/admin/book-form")
    public String bookForm(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("editId", id);
        return "admin/book-form";
    }

    /** 分类管理 */
    @GetMapping("/admin/categories")
    public String categories() { return "admin/categories"; }

    /** 读者管理 */
    @GetMapping("/admin/users")
    public String users() { return "admin/users"; }

    /** 借阅管理 */
    @GetMapping("/admin/borrows")
    public String borrows() { return "admin/borrows"; }

    /** 逾期管理 */
    @GetMapping("/admin/overdue")
    public String overdue() { return "admin/overdue"; }

    /** 权限管理 */
    @GetMapping("/admin/permissions")
    public String permissions() { return "admin/permissions"; }

    /** 系统设置 */
    @GetMapping("/admin/settings")
    public String settings() { return "admin/settings"; }
}
