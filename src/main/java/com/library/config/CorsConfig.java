package com.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置
 * <p>允许前端跨域访问后端API</p>
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")          // 拦截所有 /api/ 开头的请求
                .allowedOriginPatterns("http://localhost:*")      // 仅允许本地来源（allowCredentials=true时不能使用*）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的HTTP方法
                .allowedHeaders("*")             // 允许所有请求头
                .allowCredentials(true)          // 允许携带Cookie
                .maxAge(3600);                   // 预检请求缓存时间（秒）
    }
}
