package com.wevserver.poll;

import com.wevserver.api.PollCreate;
import com.wevserver.api.SessionDataCreate;
import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.application.feature.FeatureType;
import jakarta.validation.Valid;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequiredArgsConstructor
public class PollCreateController {

    private final PollRepository pollRepository;

    @FeatureMapping(type = FeatureType.ENTITY_CREATE, entity = Poll.class)
    @GetMapping(PollCreate.PATH)
    @PreAuthorize(
            "hasAuthority('ROLE_ADMIN') or hasAuthority('/poll') or"
                    + " hasAuthority('/poll/poll-create')")
    public ModelAndView pollCreateGet(final PollCreate.RequestParams requestParams) {

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/poll/templates/poll-create");

        modelAndView.addAllObjects(requestParams.map());

        modelAndView.addObject(
                "timeZoneOptions",
                Arrays.stream(TimeZone.getAvailableIDs())
                        .map(zoneId -> new TimezoneOption(zoneId, null))
                        .toList());

        final SessionDataCreate.RequestParams saveUriRequestParams =
                new SessionDataCreate.RequestParams();
        saveUriRequestParams.setDataId(PollCreate.PATH);
        saveUriRequestParams.setRedirectUri(PollCreate.PATH);

        modelAndView.addObject(
                "saveUri",
                UriComponentsBuilder.fromPath(SessionDataCreate.PATH)
                        .queryParams(saveUriRequestParams.map())
                        .encode()
                        .build()
                        .toString());

        return modelAndView;
    }

    @FeatureMapping(type = FeatureType.ENTITY_CREATE, entity = Poll.class)
    @PostMapping(PollCreate.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('POLL_CREATE')")
    public ModelAndView pollCreatePost(
            @Valid final PollCreate.RequestParams requestParams,
            final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext) {

        if (bindingResult.hasErrors()) {

            final ModelAndView modelAndView =
                    new ModelAndView("com/wevserver/poll/templates/poll-create");
            modelAndView.addAllObjects(requestParams.map());

            final SessionDataCreate.RequestParams saveUriRequestParams =
                    new SessionDataCreate.RequestParams();
            saveUriRequestParams.setDataId(PollCreate.PATH);
            saveUriRequestParams.setRedirectUri(PollCreate.PATH);

            modelAndView.addObject(
                    "saveUri",
                    UriComponentsBuilder.fromPath(SessionDataCreate.PATH)
                            .queryParams(saveUriRequestParams.map())
                            .encode()
                            .build()
                            .toString());

            return modelAndView;
        }

        final ZoneId timeZoneId = requestParams.getTimeZone();
        final Long startAt =
                requestParams.getStartAt().atZone(timeZoneId).toInstant().toEpochMilli();
        final Long endAt = requestParams.getEndAt().atZone(timeZoneId).toInstant().toEpochMilli();

        final Poll poll =
                pollRepository.save(
                        new Poll(
                                UUID.randomUUID().toString(),
                                null,
                                requestParams.getName(),
                                requestParams.getDetails(),
                                startAt,
                                endAt,
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName(),
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName()));

        redirectAttributes.addAttribute("id", poll.getId());
        return new ModelAndView("redirect:/poll/poll-read/{id}");
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
}
