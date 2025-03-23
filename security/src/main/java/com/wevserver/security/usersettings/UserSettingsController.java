package com.wevserver.security.usersettings;

import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.application.feature.FeatureType;
import com.wevserver.lib.FormData;
import com.wevserver.security.user.User;
import com.wevserver.security.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;

@Controller
@RequiredArgsConstructor
public class UserSettingsController {

    private final UserRepository userRepository;

    @FeatureMapping(type = FeatureType.ACTION)
    @GetMapping("/security/user-settings")
    public ModelAndView userSettingsGet(
            final HttpSession httpSession,
            final @CurrentSecurityContext SecurityContext securityContext) {

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/security/templates/user-settings");

        UserSettings userSettings;

        if (SecurityContextHolder.getContext().getAuthentication()
                instanceof AnonymousAuthenticationToken) {

            userSettings = (UserSettings) httpSession.getAttribute("userSettings");
        } else if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {

            final Optional<User> userOptional =
                    userRepository.findById(securityContext.getAuthentication().getName());

            if (Objects.nonNull(userOptional.get().getSettings())) {

                userSettings = new UserSettings(userOptional.get().getSettings().getData());
            } else {
                userSettings = null;
            }
        } else {
            userSettings = null;
        }

        if (Objects.nonNull(userSettings)) {

            modelAndView.addObject("language", userSettings.getLanguage());
            modelAndView.addObject("timeZone", userSettings.getTimeZone());
        }

        modelAndView.addObject(
                "languageOptions",
                Arrays.stream(Locale.getAvailableLocales())
                        .map(Locale::getDisplayLanguage)
                        .collect(Collectors.toList())
                        .stream()
                        .map(language -> new LanguageOption(Locale.of(language), userSettings))
                        .toList());

        modelAndView.addObject(
                "timeZoneOptions",
                Arrays.stream(TimeZone.getAvailableIDs())
                        .map(zoneId -> new TimezoneOption(zoneId, userSettings))
                        .toList());

        return modelAndView;
    }

    @PostMapping("/security/user-settings")
    public ModelAndView userSettingsPost(
            final HttpSession httpSession,
            final HttpServletRequest httpServletRequest,
            final @CurrentSecurityContext SecurityContext securityContext,
            final RequestParams requestParams) {

        UserSettings userSettings = null;
        final RequestContext requestContext = new RequestContext(httpServletRequest);

        if (SecurityContextHolder.getContext().getAuthentication()
                instanceof AnonymousAuthenticationToken) {

            userSettings = (UserSettings) httpSession.getAttribute("userSettings");

            if (userSettings == null) {

                userSettings = new UserSettings();
            }

            userSettings.setLanguage(requestParams.getLanguage());
            userSettings.setTimeZone(requestParams.getTimeZone());
            httpSession.setAttribute("userSettings", userSettings);

        } else if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {

            final Optional<User> userOptional =
                    userRepository.findById(securityContext.getAuthentication().getName());

            if (Objects.nonNull(userOptional.get().getSettings())) {

                userSettings = new UserSettings(userOptional.get().getSettings().getData());
            } else {

                userSettings = new UserSettings();
            }

            userSettings.setLanguage(requestParams.getLanguage());
            userSettings.setTimeZone(requestParams.getTimeZone());

            userOptional.get().setSettings(new FormData(userSettings.map()));
            userRepository.save(userOptional.get());
        }

        requestContext.changeLocale(
                Locale.of(userSettings.getLanguage()),
                TimeZone.getTimeZone(userSettings.getTimeZone()));

        return new ModelAndView("redirect:/security/user-settings");
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class LanguageOption {

        private String value;

        private String label;

        private String selected;

        public LanguageOption(final Locale locale, final UserSettings userSettings) {

            value = locale.getLanguage();
            label = locale.getDisplayLanguage();

            if (Objects.nonNull(userSettings)
                    && locale.getLanguage().equalsIgnoreCase(userSettings.getLanguage())) {

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

        public TimezoneOption(final String zoneId, final UserSettings userSettings) {

            value = zoneId;
            label = zoneId;

            if (Objects.nonNull(userSettings)
                    && zoneId.equalsIgnoreCase(userSettings.getTimeZone())) {

                selected = "selected";
            }
        }
    }

    @Getter
    @Setter
    private static class RequestParams {

        private String language;

        private String timeZone;
    }
}
