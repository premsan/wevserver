package com.wevserver.poll;

import com.wevserver.api.PollList;
import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.application.feature.FeatureType;
import com.wevserver.ui.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequiredArgsConstructor
public class PollListController {

    private final PollRepository pollRepository;

    @FeatureMapping(type = FeatureType.ENTITY_LIST, entity = Poll.class)
    @GetMapping(PollList.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('POLL_LIST')")
    public ModelAndView pollListGet(
            final HttpServletRequest httpServletRequest,
            final PollList.RequestParams requestParams) {

        final RequestContext requestContext = new RequestContext(httpServletRequest);

        final Poll poll = new Poll();

        if (StringUtils.hasText(requestParams.getId())) {
            poll.setId(requestParams.getId());
        }

        if (StringUtils.hasText(requestParams.getName())) {
            poll.setName(requestParams.getName());
        }

        if (StringUtils.hasText(requestParams.getCreatedBy())) {
            poll.setCreatedBy(requestParams.getCreatedBy());
        }

        if (StringUtils.hasText(requestParams.getUpdatedBy())) {
            poll.setUpdatedBy(requestParams.getUpdatedBy());
        }

        final Example<Poll> pollExample =
                Example.of(
                        poll,
                        ExampleMatcher.matching()
                                .withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                                .withIgnoreCase());

        final Page<Poll> pollPage =
                pollRepository.findAll(pollExample, requestParams.getPageable());

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/poll/templates/poll-list");

        final Page<PollView> pollViewPage =
                pollPage.map(poll1 -> new PollView(requestContext, poll1, requestParams));

        modelAndView.addObject("requestParams", requestParams);
        modelAndView.addObject("pollPage", pollViewPage);
        modelAndView.addObject(
                "pagination", new Pagination(httpServletRequest, pollViewPage).getModel());

        return modelAndView;
    }

    @Getter
    @Setter
    private static class PollView {

        private String id;

        private String name;

        private String updatedAt;

        private String _selectUri;

        public PollView(
                final RequestContext requestContext,
                final Poll poll,
                final PollList.RequestParams requestParams) {

            this.id = poll.getId();
            this.name = poll.getName();
            this.updatedAt =
                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                            .withLocale(LocaleContextHolder.getLocale())
                            .format(
                                    Instant.ofEpochMilli(poll.getUpdatedAt())
                                            .atZone(
                                                    requestContext.getTimeZone() == null
                                                            ? ZoneId.systemDefault()
                                                            : requestContext
                                                                    .getTimeZone()
                                                                    .toZoneId()));
            if (StringUtils.hasText(requestParams.getSelectUri())
                    && StringUtils.hasText(requestParams.getSelectParam())) {

                this._selectUri =
                        UriComponentsBuilder.fromUriString(requestParams.getSelectUri())
                                .queryParam(requestParams.getSelectParam(), poll.getId())
                                .toUriString();
            }
        }
    }
}
