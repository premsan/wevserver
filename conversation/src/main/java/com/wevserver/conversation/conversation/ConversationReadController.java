package com.wevserver.conversation.conversation;

import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.ui.ErrorMessagesSupplier;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class ConversationReadController {

    private final ErrorMessagesSupplier errorMessagesSupplier;
    private final ConversationRepository conversationRepository;

    @FeatureMapping
    @GetMapping("/conversation/conversation-read")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_READ')")
    public ModelAndView conversationReadGet(
            @Valid final RequestParams requestParams, final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            ModelAndView modelAndView =
                    new ModelAndView(
                            "com/wevserver/conversation/templates/conversation-read-request");

            modelAndView.addObject("requestParams", requestParams);
            modelAndView.addObject(
                    "errorMessages", errorMessagesSupplier.getErrorMessages(bindingResult));

            return modelAndView;
        }

        final Optional<Conversation> optionalConversation =
                conversationRepository.findById(requestParams.getId());

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
    private static class RequestParams {

        @NotBlank private String id;
    }
}
