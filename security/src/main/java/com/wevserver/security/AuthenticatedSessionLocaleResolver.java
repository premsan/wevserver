package com.wevserver.security;

import com.wevserver.security.userlocale.UserLocale;
import java.util.Locale;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Component
@RequiredArgsConstructor
public class AuthenticatedSessionLocaleResolver extends SessionLocaleResolver {

    private final AuthenticationTrustResolver authenticationTrustResolver;

    @Override
    protected Locale getDefaultLocale() {

        final Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authenticationTrustResolver.isAuthenticated(authentication)
                && UserLocale.class.isAssignableFrom(authentication.getPrincipal().getClass())) {

            final UserLocale userLocale =
                    (UserLocale)
                            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return Locale.of(userLocale.language(), userLocale.country());
        }

        return null;
    }

    @Override
    public TimeZone getDefaultTimeZone() {

        final Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authenticationTrustResolver.isAuthenticated(authentication)
                && UserLocale.class.isAssignableFrom(authentication.getPrincipal().getClass())) {

            final UserLocale userLocale =
                    (UserLocale)
                            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return TimeZone.getTimeZone(userLocale.timeZone());
        }

        return null;
    }
}
