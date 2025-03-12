package com.wevserver.application.entityaudit;

import com.wevserver.application.feature.Feature;
import com.wevserver.application.feature.FeatureRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class EntityAuditHandlerInterceptor implements HandlerInterceptor {

    private final FeatureRepository featureRepository;
    private final EntityAuditRepository entityAuditRepository;

    public void afterCompletion(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler,
            @Nullable final Exception exception) {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {

            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication()
                instanceof AnonymousAuthenticationToken) {

            return;
        }

        final Feature feature = featureRepository.findByPath(request.getServletPath());

        if (feature == null || feature.getEntityName() == null) {

            return;
        }

        final String principalName =
                SecurityContextHolder.getContext().getAuthentication().getName();

        EntityAudit entityAudit =
                entityAuditRepository.findByPrincipalNameAndEntityName(
                        principalName, feature.getEntityName());

        if (entityAudit == null) {

            entityAudit = new EntityAudit();
            entityAudit.setId(UUID.randomUUID().toString());
            entityAudit.setPrincipalName(principalName);
            entityAudit.setEntityName(feature.getEntityName());
            entityAudit.setCreatedAt(System.currentTimeMillis());
        }

        entityAudit.setNotified(false);
        entityAudit.setEntityAccessedAt(System.currentTimeMillis());
        entityAudit.setEntityCreatedCount(0L);
        entityAudit.setEntityUpdatedCount(0L);

        entityAuditRepository.save(entityAudit);
    }
}
