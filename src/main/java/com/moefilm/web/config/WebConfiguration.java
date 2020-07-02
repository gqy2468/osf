package com.moefilm.web.config;

import com.moefilm.web.interceptor.LoginInterceptor;
import com.moefilm.web.interceptor.CommonInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web层配置
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private CommonInterceptor commonInterceptor;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor);
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/css/**", "/files/*", "/fonts/*", "/img/**", "/js/*", "/welcome", "/account/*", "/album/upload/media/", "/media/upload/");
    }
}
