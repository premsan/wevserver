package com.wevserver.application;

import com.wevserver.application.entityaudit.EntityAuditHandlerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@ComponentScan("com.wevserver")
@EnableJdbcRepositories("com.wevserver.application")
public class ApplicationConfiguration implements WebMvcConfigurer {

    private final EntityAuditHandlerInterceptor entityAuditHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(entityAuditHandlerInterceptor);
    }
}
