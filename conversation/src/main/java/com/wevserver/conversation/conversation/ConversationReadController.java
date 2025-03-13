package com.wevserver.conversation.conversation;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
        modelAndView.addObject("conversation", conversationOptional.get());

        return modelAndView;
    }
}
