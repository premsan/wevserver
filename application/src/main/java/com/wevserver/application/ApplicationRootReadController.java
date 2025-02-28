package com.wevserver.application;

import com.wevserver.application.feature.Feature;
import com.wevserver.application.feature.FeatureMapping;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class ApplicationRootReadController {

    private final String FAVOURITE_URI_SESSION_ATTR_NAME = "favourite.uri";

    private final Pattern authorityPattern = Pattern.compile("hasAuthority\\('(.*?)'\\)");

    private final ApplicationContext applicationContext;
    private final Map<String, List<Feature>> moduleFeatures = new HashMap<>();

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
                final PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);

                final Feature feature = new Feature();
                feature.setModule(module);
                feature.setPath(featureGetMapping.value()[0]);
                feature.setPriority(featureMapping.priority());
                feature.setMessageCode(
                        controllerClass.getSimpleName().concat(".").concat(method.getName()));

                if (Objects.nonNull(preAuthorize)) {

                    final List<String> authorities = new ArrayList<>();

                    final Matcher authorityMatcher = authorityPattern.matcher(preAuthorize.value());
                    while (authorityMatcher.find()) {

                        authorities.add(authorityMatcher.group(1));
                    }
                    feature.setPreAuthorizeAuthorities(authorities);
                }

                List<Feature> moduleFeatures = this.moduleFeatures.get(module);
                if (moduleFeatures == null) {

                    moduleFeatures = new ArrayList<>();
                    this.moduleFeatures.put(feature.getModule(), moduleFeatures);
                }
                moduleFeatures.add(feature);
            }
        }

        for (final List<Feature> moduleFeatures : moduleFeatures.values()) {

            Collections.sort(moduleFeatures, Comparator.comparing(Feature::getPriority));
        }
    }

    @GetMapping
    public ModelAndView applicationRootReadGet(final HttpSession httpSession) {

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/application/templates/application-root");

        final Set<String> favouriteList =
                (Set<String>) httpSession.getAttribute(FAVOURITE_URI_SESSION_ATTR_NAME);

        if (Objects.nonNull(favouriteList)) {
            modelAndView.addObject(
                    "favouriteList",
                    favouriteList.stream().map(e -> new FeatureItem(e, favouriteList)).toList());
        }

        modelAndView.addObject("moduleList", moduleFeatures.keySet());
        modelAndView.addObject(
                "moduleFeatureList",
                moduleFeatures.entrySet().stream()
                        .map(
                                e ->
                                        new ModuleItem(
                                                e.getKey(),
                                                e.getValue().stream()
                                                        .map(Feature::getPath)
                                                        .map(p -> new FeatureItem(p, favouriteList))
                                                        .collect(Collectors.toList())))
                        .toList());

        return modelAndView;
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

        private FeatureItem(final String path, final Set<String> favouriteList) {

            this.path = path;

            final String[] components = path.split("/");
            this.text = components[components.length - 1];

            if (Objects.nonNull(favouriteList)) {

                this.favourite = favouriteList.contains(path);
            }
        }
    }
}
