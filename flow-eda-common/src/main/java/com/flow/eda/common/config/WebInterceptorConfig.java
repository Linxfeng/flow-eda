package com.flow.eda.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** 自定义web请求拦截器 */
@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {

    @Autowired private Oauth2HandlerInterceptor oauth2HandlerInterceptor;

    /** 添加自定义拦截器 */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(oauth2HandlerInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/oauth/**");
    }
}
