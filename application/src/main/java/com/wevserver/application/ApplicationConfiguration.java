package com.wevserver.application;

import com.wevserver.application.entityaudit.EntityAuditHandlerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@RequiredArgsConstructor
@ComponentScan("com.wevserver")
@EnableJdbcRepositories("com.wevserver.application")
public class ApplicationConfiguration implements WebMvcConfigurer {

    private final Jackson2JsonViewResolver jackson2JsonViewResolver;
    private final EntityAuditHandlerInterceptor entityAuditHandlerInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(entityAuditHandlerInterceptor);
    }

    @Override
    public void configureViewResolvers(final ViewResolverRegistry registry) {
        registry.viewResolver(jackson2JsonViewResolver);
    }

    @Bean
    public LocaleResolver localeResolver() {

        return new SessionLocaleResolver();
    }
}
