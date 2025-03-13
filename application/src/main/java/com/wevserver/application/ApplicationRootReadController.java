package com.wevserver.application;

import com.wevserver.application.entityaudit.EntityAudit;
import com.wevserver.application.entityaudit.EntityAuditRepository;
import com.wevserver.application.feature.Feature;
import com.wevserver.application.feature.FeatureRepository;
import com.wevserver.application.feature.FeatureType;
import jakarta.servlet.http.HttpSession;
import java.text.NumberFormat;
import java.util.ArrayList;
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
                    featureItems(
                            favourites.stream()
                                    .map(e -> featureRepository.findByPath(e))
                                    .filter(Objects::nonNull)
                                    .toList(),
                            entityAuditByEntityName,
                            favourites));
        }

        modelAndView.addObject("modules", featureRepository.getFeaturesByModule().keySet());
        modelAndView.addObject(
                "moduleFeatures",
                featureRepository.getFeaturesByModule().entrySet().stream()
                        .map(
                                e ->
                                        new ModuleItem(
                                                e.getKey(),
                                                featureItems(
                                                        e.getValue(),
                                                        entityAuditByEntityName,
                                                        favourites)))
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

        private String createPath;

        private String path;

        private String label;

        private String counter;

        private Boolean favourite;

        private FeatureItem(
                final Feature feature,
                final Map<String, EntityAudit> entityAuditByEntityName,
                final Set<String> favouritePaths) {

            final String[] components = feature.getPath().split("/");
            final String component = components[components.length - 1];

            path = feature.getPath();
            label = component;

            final EntityAudit entityAudit =
                    entityAuditByEntityName.get(feature.getEntity().getName());

            if (Objects.nonNull(entityAudit)) {

                final Long countSum =
                        entityAudit.getEntityCreatedCount() + entityAudit.getEntityUpdatedCount();

                if (countSum > 0) {

                    counter = numberFormat.format(countSum);
                }
            }

            if (Objects.nonNull(favouritePaths)) {

                favourite = favouritePaths.contains(path);
            }
        }
    }

    private List<FeatureItem> featureItems(
            final List<Feature> features,
            final Map<String, EntityAudit> entityAuditByEntityName,
            final Set<String> favouritePaths) {

        final List<FeatureItem> featureItems = new ArrayList<>();

        for (final Feature feature : features) {

            if (feature.getType() == FeatureType.ENTITY_CREATE) {

                continue;
            }

            final FeatureItem featureItem =
                    new FeatureItem(feature, entityAuditByEntityName, favouritePaths);

            if (feature.getType() == FeatureType.ENTITY_LIST) {

                final Feature createFeature =
                        featureRepository.findByEntityAndType(
                                feature.getEntity(), FeatureType.ENTITY_CREATE);

                if (Objects.nonNull(createFeature)) {
                    featureItem.setCreatePath(createFeature.getPath());
                }
            }

            featureItems.add(featureItem);
        }

        return featureItems;
    }
}
