package com.wevserver.poll;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class PollReadController {

    private final PollRepository pollRepository;

    @GetMapping("/poll/poll-read/{id}")
    @PreAuthorize(
            "hasAuthority('ROLE_ADMIN') or hasAuthority('/poll') or hasAuthority('/poll/poll-read')"
                    + " or hasPermission(#id, '/poll/poll-read')")
    public ModelAndView pollReadGet(@PathVariable final String id) {

        final Optional<Poll> pollOptional = pollRepository.findById(id);

        if (pollOptional.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/poll/templates/poll-read");
        modelAndView.addObject("poll", new PollView(pollOptional.get()));

        return modelAndView;
    }

    @Getter
    @Setter
    private static class PollView {

        private String id;

        private Long version;

        private String name;

        private String createdAt;

        private String createdBy;

        private String updatedAt;

        private String updatedBy;

        public PollView(final Poll poll) {

            this.id = poll.getId();
            this.version = poll.getVersion();
            this.name = poll.getName();
            this.createdAt =
                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                            .withLocale(LocaleContextHolder.getLocale())
                            .format(
                                    Instant.ofEpochMilli(poll.getCreatedAt())
                                            .atZone(ZoneId.systemDefault()));
            this.createdBy = poll.getCreatedBy();
            this.updatedAt =
                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                            .withLocale(LocaleContextHolder.getLocale())
                            .format(
                                    Instant.ofEpochMilli(poll.getUpdatedAt())
                                            .atZone(ZoneId.systemDefault()));
            this.updatedBy = poll.getUpdatedBy();
        }
    }
}
