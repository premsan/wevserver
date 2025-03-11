package com.wevserver.application;

import com.wevserver.application.entityaudit.EntityAudit;
import com.wevserver.application.entityaudit.EntityAuditRepository;
import com.wevserver.application.feature.Feature;
import com.wevserver.application.feature.FeatureRepository;
import jakarta.servlet.http.HttpSession;
import java.util.List;
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

        final Set<String> favouriteList =
                (Set<String>) httpSession.getAttribute(FAVOURITE_URI_SESSION_ATTR_NAME);

        if (Objects.nonNull(favouriteList)) {
            modelAndView.addObject(
                    "favouriteList",
                    favouriteList.stream()
                            .map(
                                    e ->
                                            new FeatureItem(
                                                    e,
                                                    customizeLabel(
                                                            entityAuditByEntityName,
                                                            featureRepository.findByPath(e)),
                                                    favouriteList))
                            .toList());
        }

        modelAndView.addObject("moduleList", featureRepository.getFeaturesByModule().keySet());
        modelAndView.addObject(
                "moduleFeatureList",
                featureRepository.getFeaturesByModule().entrySet().stream()
                        .map(
                                e ->
                                        new ModuleItem(
                                                e.getKey(),
                                                e.getValue().stream()
                                                        .map(
                                                                f ->
                                                                        new FeatureItem(
                                                                                f.getPath(),
                                                                                customizeLabel(
                                                                                        entityAuditByEntityName,
                                                                                        f),
                                                                                favouriteList))
                                                        .collect(Collectors.toList())))
                        .toList());

        return modelAndView;
    }

    private String customizeLabel(
            final Map<String, EntityAudit> entityAuditMap, final Feature feature) {

        final String[] components = feature.getPath().split("/");
        final String component = components[components.length - 1];

        final EntityAudit entityAudit = entityAuditMap.get(feature.getEntityName());

        if (entityAudit == null || entityAudit.getEntityCreatedCount() == 0) {

            return component;
        }

        return component + " (" + entityAudit.getEntityCreatedCount() + ")";
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class ModuleItem {

        private String name;

        private List<FeatureItem> featureList;
    }

    @Getter
    @Setter
    private class FeatureItem {

        private String path;

        private String text;

        private Boolean favourite;

        private FeatureItem(final String path, String text, final Set<String> favouriteList) {

            this.path = path;
            this.text = text;

            if (Objects.nonNull(favouriteList)) {

                this.favourite = favouriteList.contains(path);
            }
        }
    }
}
