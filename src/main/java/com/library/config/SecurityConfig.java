package com.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全配置
 * <p>提供BCrypt密码编码器Bean</p>
 */
@Configuration
public class SecurityConfig {

    /**
     * BCrypt 密码加密器
     * <p>用于用户密码的加密存储和校验</p>
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
