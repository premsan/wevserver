package com.wevserver.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
@RequiredArgsConstructor
public class ErrorMessagesSupplier {

    private final MessageSource messageSource;

    public ErrorMessages get(final Errors errors) {

        return new ErrorMessages(messageSource, LocaleContextHolder.getLocale(), errors);
    }
}
