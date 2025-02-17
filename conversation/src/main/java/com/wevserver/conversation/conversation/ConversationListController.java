package com.wevserver.conversation.conversation;

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
public class ConversationListController {

    private final ConversationRepository conversationRepository;

    @FeatureMapping
    @GetMapping("/conversation/conversation-list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_CONVERSATION_LIST')")
    public ModelAndView conversationListGet(@PathVariable String id) {

        final Optional<Conversation> optionalConversation = conversationRepository.findById(id);

        if (optionalConversation.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/conversation/templates/conversation-read");
        modelAndView.addObject("conversation", optionalConversation.get());

        return modelAndView;
    }
}
