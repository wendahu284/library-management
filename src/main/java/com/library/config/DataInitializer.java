package com.library.config;

import com.library.entity.Permission;
import com.library.entity.SystemConfig;
import com.library.entity.User;
import com.library.mapper.PermissionMapper;
import com.library.mapper.RolePermissionMapper;
import com.library.mapper.SystemConfigMapper;
import com.library.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 数据初始化器
 * <p>项目启动时检查并创建默认管理员账号和测试读者账号</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SystemConfigMapper systemConfigMapper;
    private final PermissionMapper permissionMapper;
    private final RolePermissionMapper rolePermissionMapper;

    @Override
    public void run(String... args) {
        // 初始化管理员
        initUser("admin", "admin123", "系统管理员", 1);
        // 初始化测试读者
        initUser("zhangsan", "123456", "张三", 0);
        initUser("lisi",     "123456", "李四", 0);
        initUser("wangwu",   "123456", "王五", 0);

        // 初始化系统参数
        initConfig("library.borrow.max-count", "5", "每位读者最大同时借阅数量", 1);
        initConfig("library.borrow.duration-days", "30", "单次借阅期限（天）", 1);
        initConfig("server.port", "8080", "服务器端口", 0);
        initConfig("database.name", "library_db", "MySQL数据库名", 0);

        // 初始化权限
        initPermissions();

        log.info("默认账号初始化完成");
    }

    private void initUser(String username, String rawPassword, String realName, int role) {
        User existing = userMapper.selectByUsername(username);
        if (existing == null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setRealName(realName);
            user.setRole(role);
            user.setStatus(1);
            userMapper.insert(user);
            log.info("账号已创建: {} / {}", username, rawPassword);
        }
    }

    private void initConfig(String key, String value, String description, int editable) {
        SystemConfig existing = systemConfigMapper.selectByKey(key);
        if (existing == null) {
            SystemConfig config = new SystemConfig();
            config.setConfigKey(key);
            config.setConfigValue(value);
            config.setDescription(description);
            config.setEditable(editable);
            systemConfigMapper.insert(config);
            log.info("系统参数已初始化: {} = {}", key, value);
        }
    }

    private void initPermissions() {
        List<Permission> existing = permissionMapper.selectAll();
        if (existing != null && !existing.isEmpty()) {
            return; // 已初始化
        }

        // 定义所有可用权限
        String[][] permDefs = {
            {"图书管理", "book:manage", "新增/修改/删除图书"},
            {"读者管理", "user:manage", "启用/禁用读者账号"},
            {"借阅管理", "borrow:manage", "审批归还/管理借阅"},
            {"分类管理", "category:manage", "管理图书分类"},
            {"系统设置", "settings:manage", "修改系统参数/管理公告"},
            {"权限管理", "permission:manage", "分配角色权限"},
        };

        for (String[] def : permDefs) {
            Permission perm = new Permission();
            perm.setName(def[0]);
            perm.setCode(def[1]);
            perm.setDescription(def[2]);
            permissionMapper.insert(perm);
        }

        // 重新查询以获取ID
        List<Permission> perms = permissionMapper.selectAll();
        List<Long> allIds = perms.stream().map(Permission::getId).toList();
        List<Long> readerIds = perms.stream()
                .filter(p -> "book:manage".equals(p.getCode()))
                .map(Permission::getId).toList();

        // 管理员拥有全部权限
        rolePermissionMapper.insertBatch(1, allIds);
        // 读者仅拥有图书查看权限
        rolePermissionMapper.insertBatch(0, readerIds);

        log.info("权限初始化完成: {} 个权限项", perms.size());
    }
}
