package com.wevserver.application.feature;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Component
@RequiredArgsConstructor
public class FeatureRepository {

    private final Map<String, List<Feature>> featuresByModule = new HashMap<>();
    private final Map<String, Feature> featureByPath = new HashMap<>();
    private final Map<Class<?>, Map<FeatureType, Feature>> featureByTypeByEntity = new HashMap<>();

    private final ApplicationContext applicationContext;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReadyEvent() {

        final Map<String, Object> controllers =
                applicationContext.getBeansWithAnnotation(Controller.class);

        for (final Object controller : controllers.values()) {

            final Class<?> controllerClass = AopUtils.getTargetClass(controller);

            for (final Method method : controllerClass.getDeclaredMethods()) {

                final FeatureMapping featureMapping = method.getAnnotation(FeatureMapping.class);
                final GetMapping featureGetMapping = method.getAnnotation(GetMapping.class);

                if (featureMapping == null
                        || featureGetMapping == null
                        || featureGetMapping.value().length == 0) {

                    continue;
                }

                final String module = controllerClass.getPackageName().split("\\.")[2];

                final Feature feature = new Feature();
                feature.setModule(module);
                feature.setPath(featureGetMapping.value()[0]);
                feature.setMessageCode(
                        controllerClass.getSimpleName().concat(".").concat(method.getName()));
                feature.setEntity(featureMapping.entity());
                feature.setType(featureMapping.type());

                Map<FeatureType, Feature> featureByType =
                        featureByTypeByEntity.get(feature.getEntity());
                if (featureByType == null) {

                    featureByType = new HashMap<>();
                    featureByTypeByEntity.put(feature.getEntity(), featureByType);
                }
                featureByType.put(feature.getType(), feature);

                List<Feature> features = featuresByModule.get(module);
                if (features == null) {

                    features = new ArrayList<>();
                    featuresByModule.put(feature.getModule(), features);
                }
                features.add(feature);
                featureByPath.put(feature.getPath(), feature);
            }
        }

        for (final List<Feature> moduleFeatures : featuresByModule.values()) {

            Collections.sort(moduleFeatures, Comparator.comparing(Feature::getPath));
        }
    }

    public Feature findByPath(final String path) {

        return featureByPath.get(path);
    }

    public Feature findByEntityAndType(final Class<?> entity, final FeatureType type) {

        return featureByTypeByEntity.get(entity).get(type);
    }
}
