package com.wevserver.ui;

import com.samskivert.mustache.Mustache;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class InternationalizationAdvice {

    private final MessageSource messageSource;

    @ModelAttribute("i18n")
    public Mustache.Lambda i18n(final Locale locale) {
        return (frag, out) -> {
            final String code = frag.execute();
            final String message = messageSource.getMessage(code.strip(), null, code, locale);
            out.write(message);
        };
    }
}
