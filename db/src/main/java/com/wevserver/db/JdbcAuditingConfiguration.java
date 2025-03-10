package com.wevserver.db;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@Configuration
@EnableJdbcAuditing
@RequiredArgsConstructor
public class JdbcAuditingConfiguration {

    @Bean
    AuditorAware<User> auditorProvider() {
        return new AuditorAwareImpl();
    }

    class AuditorAwareImpl implements AuditorAware<User> {

        @Override
        public Optional<User> getCurrentAuditor() {

            return Optional.ofNullable(SecurityContextHolder.getContext())
                    .map(SecurityContext::getAuthentication)
                    .filter(Authentication::isAuthenticated)
                    .map(Authentication::getPrincipal)
                    .map(User.class::cast);
        }
    }
}
