package com.wevserver.application;

import com.wevserver.application.entityaudit.EntityAudit;
import com.wevserver.application.entityaudit.EntityAuditRepository;
import com.wevserver.application.feature.Feature;
import com.wevserver.application.feature.FeatureRepository;
import jakarta.servlet.http.HttpSession;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class ApplicationRootReadController {

    private final String FAVOURITE_URI_SESSION_ATTR_NAME = "favourite.uri";
    private final NumberFormat numberFormat =
            NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);

    private final FeatureRepository featureRepository;
    private final EntityAuditRepository entityAuditRepository;

    @GetMapping
    public ModelAndView applicationRootReadGet(
            final HttpSession httpSession,
            @CurrentSecurityContext final SecurityContext securityContext) {

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/application/templates/application-root");

        final Map<String, EntityAudit> entityAuditByEntityName =
                entityAuditRepository
                        .findByPrincipalName(securityContext.getAuthentication().getName())
                        .stream()
                        .collect(Collectors.toMap(EntityAudit::getEntityName, item -> item));

        final Set<String> favourites =
                (Set<String>) httpSession.getAttribute(FAVOURITE_URI_SESSION_ATTR_NAME);

        if (Objects.nonNull(favourites)) {
            modelAndView.addObject(
                    "favourites",
                    favourites.stream()
                            .map(
                                    e ->
                                            new FeatureItem(
                                                    featureRepository.findByPath(e),
                                                    entityAuditByEntityName,
                                                    favourites))
                            .toList());
        }

        modelAndView.addObject("modules", featureRepository.getFeaturesByModule().keySet());
        modelAndView.addObject(
                "moduleFeatures",
                featureRepository.getFeaturesByModule().entrySet().stream()
                        .map(
                                e ->
                                        new ModuleItem(
                                                e.getKey(),
                                                e.getValue().stream()
                                                        .map(
                                                                f ->
                                                                        new FeatureItem(
                                                                                f,
                                                                                entityAuditByEntityName,
                                                                                favourites))
                                                        .collect(Collectors.toList())))
                        .toList());

        return modelAndView;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class ModuleItem {

        private String name;

        private List<FeatureItem> features;
    }

    @Getter
    @Setter
    private class FeatureItem {

        private String path;

        private String text;

        private String counter;

        private Boolean favourite;

        private FeatureItem(
                final Feature feature,
                final Map<String, EntityAudit> entityAuditByEntityName,
                final Set<String> favouritePaths) {

            final String[] components = feature.getPath().split("/");
            final String component = components[components.length - 1];

            path = feature.getPath();
            text = component;

            final EntityAudit entityAudit =
                    entityAuditByEntityName.get(feature.getEntity().getName());

            if (Objects.nonNull(entityAudit)) {

                counter =
                        numberFormat.format(
                                entityAudit.getEntityCreatedCount()
                                        + entityAudit.getEntityUpdatedCount());
            }

            if (Objects.nonNull(favouritePaths)) {

                favourite = favouritePaths.contains(path);
            }
        }
    }
}
