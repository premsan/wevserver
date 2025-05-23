package com.wevserver.security.userlocale;

import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.application.feature.FeatureType;
import com.wevserver.security.user.User;
import com.wevserver.security.user.UserRepository;
import com.wevserver.ui.ErrorMessages;
import com.wevserver.ui.ErrorMessagesSupplier;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;

@Controller
@RequiredArgsConstructor
public class UserLocaleController {

    private final AuthenticationTrustResolver authenticationTrustResolver;
    private final ErrorMessagesSupplier errorMessagesSupplier;
    private final UserRepository userRepository;

    @FeatureMapping(type = FeatureType.ACTION)
    @GetMapping("/security/user-locale")
    public ModelAndView userLocaleGet(final HttpServletRequest httpServletRequest) {

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/security/templates/user-locale");

        final RequestContext requestContext = new RequestContext(httpServletRequest);

        addViewObjects(requestContext, modelAndView);

        return modelAndView;
    }

    @PostMapping("/security/user-locale")
    public ModelAndView userLocalePost(
            final HttpServletRequest httpServletRequest,
            @CurrentSecurityContext final SecurityContext securityContext,
            @Valid final RequestParams requestParams,
            final BindingResult bindingResult) {

        final RequestContext requestContext = new RequestContext(httpServletRequest);

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/security/templates/user-locale");

        if (bindingResult.hasErrors()) {

            final ErrorMessages errorMessages = errorMessagesSupplier.get(bindingResult);

            addViewObjects(requestContext, modelAndView);

            if (Objects.nonNull(errorMessages)) {

                modelAndView.addObject("fieldErrors", errorMessages.getFieldErrors());
            }

            return modelAndView;
        }

        final Authentication authentication = securityContext.getAuthentication();

        if (authenticationTrustResolver.isAuthenticated(authentication)) {

            final Optional<User> userOptional = userRepository.findById(authentication.getName());

            userOptional.get().setCountry(requestParams.getCountry());
            userOptional.get().setLanguage(requestParams.getLanguage());
            userOptional.get().setTimeZone(requestParams.getTimeZone());

            userRepository.save(userOptional.get());
        }

        requestContext.changeLocale(
                Locale.of(requestParams.getLanguage(), requestParams.getCountry()),
                TimeZone.getTimeZone(requestParams.getTimeZone()));

        return new ModelAndView("redirect:/security/user-locale");
    }

    private void addViewObjects(
            final RequestContext requestContext, final ModelAndView modelAndView) {

        modelAndView.addObject("country", requestContext.getLocale().getCountry());
        modelAndView.addObject("language", requestContext.getLocale().getLanguage());
        modelAndView.addObject(
                "timeZone",
                requestContext.getTimeZone() == null ? null : requestContext.getTimeZone().getID());

        modelAndView.addObject(
                "countryOptions",
                Arrays.stream(Locale.getISOCountries())
                        .map(country -> new CountryOption(country, requestContext.getLocale()))
                        .toList());

        modelAndView.addObject(
                "languageOptions",
                Arrays.stream(Locale.getISOLanguages())
                        .map(language -> new LanguageOption(language, requestContext.getLocale()))
                        .toList());

        modelAndView.addObject(
                "timeZoneOptions",
                Arrays.stream(TimeZone.getAvailableIDs())
                        .map(zoneId -> new TimezoneOption(zoneId, requestContext.getTimeZone()))
                        .toList());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class LanguageOption {

        private String value;

        private String label;

        private String selected;

        public LanguageOption(final String language, final Locale userLocale) {

            value = language;
            label = language;

            if (Objects.nonNull(userLocale)
                    && language.equalsIgnoreCase(userLocale.getLanguage())) {

                selected = "selected";
            }
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class CountryOption {

        private String value;

        private String label;

        private String selected;

        public CountryOption(final String country, final Locale userLocale) {

            value = country;
            label = country;

            if (Objects.nonNull(userLocale) && country.equalsIgnoreCase(userLocale.getCountry())) {

                selected = "selected";
            }
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class TimezoneOption {

        private String value;

        private String label;

        private String selected;

        public TimezoneOption(final String zoneId, final TimeZone userTimeZone) {

            value = zoneId;
            label = zoneId;

            if (Objects.nonNull(userTimeZone) && zoneId.equalsIgnoreCase(userTimeZone.getID())) {

                selected = "selected";
            }
        }
    }

    @Getter
    @Setter
    private static class RequestParams {

        @NotBlank private String country;

        @NotBlank private String language;

        @NotBlank private String timeZone;
    }
}
