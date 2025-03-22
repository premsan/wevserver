package com.wevserver.conversation.conversation;

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
public class ConversationReadController {

    private final ConversationRepository conversationRepository;

    @GetMapping("/conversation/conversation-read/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_READ')")
    public ModelAndView conversationReadGet(@PathVariable final String id) {

        final Optional<Conversation> conversationOptional = conversationRepository.findById(id);

        if (conversationOptional.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/conversation/templates/conversation-read");
        modelAndView.addObject("conversation", new ConversationView(conversationOptional.get()));

        return modelAndView;
    }

    @Getter
    @Setter
    private static class ConversationView {

        private String id;

        private Long version;

        private String name;

        private String createdAt;

        private String createdBy;

        private String updatedAt;

        private String updatedBy;

        public ConversationView(final Conversation conversation) {

            this.id = conversation.getId();
            this.version = conversation.getVersion();
            this.name = conversation.getName();
            this.createdAt =
                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                            .withLocale(LocaleContextHolder.getLocale())
                            .format(
                                    Instant.ofEpochMilli(conversation.getCreatedAt())
                                            .atZone(ZoneId.systemDefault()));
            this.createdBy = conversation.getCreatedBy();
            this.updatedAt =
                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                            .withLocale(LocaleContextHolder.getLocale())
                            .format(
                                    Instant.ofEpochMilli(conversation.getUpdatedAt())
                                            .atZone(ZoneId.systemDefault()));
            this.updatedBy = conversation.getUpdatedBy();
        }
    }
}
