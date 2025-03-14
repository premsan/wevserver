package com.wevserver.application;

import java.util.Locale;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Component
public class Jackson2JsonViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(final String viewName, final Locale locale) {

        return new MappingJackson2JsonView();
    }
}
