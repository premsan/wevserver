package com.wevserver.conversation.conversation;

import com.wevserver.application.feature.FeatureMapping;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.UUID;
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

@Controller
@RequiredArgsConstructor
public class ConversationCreateController {

    private final ConversationRepository conversationRepository;

    @FeatureMapping
    @GetMapping("/conversation/conversation-create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_CREATE')")
    public ModelAndView conversationCreateGet(final RequestParams requestParams) {

        final ModelAndView model =
                new ModelAndView("com/wevserver/conversation/templates/conversation-create");

        model.addObject("conversationCreate", requestParams);

        return model;
    }

    @PostMapping("/conversation/conversation-create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_CREATE')")
    public ModelAndView conversationCreatePost(
            @Valid RequestParams requestParams,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext) {

        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {

            modelAndView.setViewName("com/wevserver/conversation/templates/conversation-create");
            modelAndView.addObject("requestParams", requestParams);
            modelAndView.addObject("fieldErrors", bindingResult.getFieldErrors());
            return modelAndView;
        }

        final Conversation conversation =
                conversationRepository.save(
                        new Conversation(
                                UUID.randomUUID().toString(),
                                null,
                                requestParams.getName(),
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName()));

        redirectAttributes.addAttribute("id", conversation.getId());
        return new ModelAndView("redirect:/conversation/conversation-read");
    }

    @Getter
    @Setter
    private static class RequestParams {

        @Min(10)
        private String name;
    }
}
