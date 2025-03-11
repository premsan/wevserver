package com.wevserver.ui;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@Getter
@Setter
public class ErrorMessages {

    private final Map<String, List<String>> fieldErrors;

    private final List<String> globalErrors;

    public ErrorMessages(
            final MessageSource messageSource, final Locale locale, final Errors errors) {

        this.fieldErrors =
                errors.getFieldErrors().stream()
                        .collect(
                                Collectors.groupingBy(
                                        FieldError::getField,
                                        Collectors.mapping(
                                                fieldError ->
                                                        messageSource.getMessage(
                                                                fieldError, locale),
                                                Collectors.toList())));

        this.globalErrors =
                errors.getGlobalErrors().stream()
                        .map(objectError -> messageSource.getMessage(objectError, locale))
                        .collect(Collectors.toList());
    }

    public void addTo(final ModelMap modelMap) {

        modelMap.addAttribute("fieldErrors", fieldErrors);
        modelMap.addAttribute("globalErrors", globalErrors);
    }
}
