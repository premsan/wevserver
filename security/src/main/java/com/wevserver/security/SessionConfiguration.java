package com.wevserver.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.servlet.LocaleResolver;

@Configuration
@EnableJdbcHttpSession
@RequiredArgsConstructor
public class SessionConfiguration {

    private final AuthenticatedSessionLocaleResolver authenticatedSessionLocaleResolver;

    @Bean
    public LocaleResolver localeResolver() {

        return authenticatedSessionLocaleResolver;
    }
}
