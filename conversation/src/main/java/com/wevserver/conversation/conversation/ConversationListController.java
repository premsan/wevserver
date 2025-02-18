package com.wevserver.conversation.conversation;

import com.wevserver.application.feature.FeatureMapping;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class ConversationListController {

    private final ConversationRepository conversationRepository;

    @FeatureMapping
    @GetMapping("/conversation/conversation-list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_CONVERSATION_LIST')")
    public ModelAndView conversationListGet(RequestParams requestParams) {

        final Optional<Conversation> optionalConversation = conversationRepository.findById(null);

        if (optionalConversation.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/conversation/templates/conversation-read");
        modelAndView.addObject("conversation", optionalConversation.get());

        return modelAndView;
    }

    @Getter
    @Setter
    public static class RequestParams {

        private String id;

        private String name;

        @Min(0)
        private Integer pageNumber = 0;

        @Min(0)
        @Max(1024)
        private Integer pageSize = 32;
    }
}
