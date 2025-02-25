package com.wevserver.conversation.conversationreply;

import com.wevserver.application.feature.FeatureMapping;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class ConversationReplyReadController {

    private final ConversationReplyRepository conversationReplyRepository;

    @FeatureMapping
    @GetMapping("/conversation/conversation-reply-read/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_REPLY_READ')")
    public ModelAndView conversationReplyReadGet(@PathVariable final String id) {

        final Optional<ConversationReply> conversationReplyOptional =
                conversationReplyRepository.findById(id);

        if (conversationReplyOptional.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/conversation/templates/conversation-reply-read");
        modelAndView.addObject("conversationReply", conversationReplyOptional.get());

        return modelAndView;
    }
}
