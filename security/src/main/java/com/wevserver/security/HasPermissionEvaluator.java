package com.wevserver.security;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HasPermissionEvaluator {

    private static final String PATH_SEPARATOR = "/";

    private final HttpServletRequest httpServletRequest;

    public boolean decide(
            final MethodSecurityExpressionOperations operations,
            final String[] permission,
            final String[] targetId) {

        if (operations.hasAuthority(PATH_SEPARATOR)) {

            return true;
        }

        final Set<String> permissions = new HashSet<>();

        if (permission.length == 0) {

            permissions.addAll(expandPermission(httpServletRequest.getServletPath()));
        } else {
            for (final String p : permission) {

                permissions.addAll(expandPermission(p));
            }
        }

        return operations.hasAnyAuthority(permissions.toArray(new String[0]));
    }

    private Set<String> expandPermission(final String permission) {

        final Set<String> permissions = new HashSet<>();

        final String[] parts = permission.split(PATH_SEPARATOR);

        if (parts.length == 1) {

            permissions.add(parts[0]);
            return permissions;
        }

        permissions.add(PATH_SEPARATOR);

        final StringBuilder stringBuilder = new StringBuilder();

        for (final String part : parts) {

            if (part.isEmpty()) {
                continue;
            }

            stringBuilder.append(PATH_SEPARATOR).append(part);
            permissions.add(stringBuilder.toString());
        }

        return permissions;
    }
}
