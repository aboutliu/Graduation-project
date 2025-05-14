package org.example.springbootdemo.config;

import org.example.springbootdemo.controller.PortController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PortWebConfig implements WebMvcConfigurer {

    private final PortController portController;

    @Autowired
    public PortWebConfig(PortController portController) {
        this.portController = portController;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String frontendPort = portController.getFrontendPort();

        System.out.println("Fetching frontend port: " + frontendPort);  // 添加日志
        if (frontendPort != null) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:" + frontendPort)  // 动态设置允许的前端端口
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedHeaders("*")
                    .allowCredentials(true);
        } else {
            // 如果没有前端端口，则使用默认的端口
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:5173")  // 默认允许的端口
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedHeaders("*")
                    .allowCredentials(true);
        }
        registry.addMapping("/**")
            .allowedOriginPatterns("http://localhost:*") // ✅ 支持多个端口
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);  // ✅ 可与 patterns 配合使用
}
}