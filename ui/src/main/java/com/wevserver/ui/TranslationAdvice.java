package com.wevserver.ui;

import com.samskivert.mustache.Mustache;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class TranslationAdvice {

    private final MessageSource messageSource;

    @ModelAttribute("t")
    public Mustache.Lambda t(final Locale locale) {
        return (frag, out) -> {
            final String code = frag.execute().strip();
            final String message = messageSource.getMessage(code, null, code, locale);
            out.write(message);
        };
    }
}
