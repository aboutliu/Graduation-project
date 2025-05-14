package org.example.springbootdemo.activity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration("ActivityWebConfig")
public class ActivityWebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射 /uploads/** 到后端项目根目录的 images 文件夹
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:images/");
    }
}