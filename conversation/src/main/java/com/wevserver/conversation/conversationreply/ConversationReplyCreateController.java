package com.wevserver.conversation.conversationreply;

import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.conversation.conversation.ConversationRepository;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ConversationReplyCreateController {

    private final ConversationRepository conversationRepository;
    private final ConversationReplyRepository conversationReplyRepository;

    @FeatureMapping
    @GetMapping("/conversation/conversation-reply-create")
    @PreAuthorize(
            "hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_CONVERSATION_REPLY_CREATE')")
    public ModelAndView conversationReplyCreateGet(final RequestParameters requestParameters) {

        final ModelAndView model =
                new ModelAndView("com/wevserver/conversation/templates/conversation-reply-create");

        model.addObject("conversationReplyCreate", requestParameters);

        return model;
    }

    @PostMapping("/conversation/conversation-reply-create")
    @PreAuthorize(
            "hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_CONVERSATION_REPLY_CREATE')")
    public ModelAndView conversationReplyCreatePost(
            @Valid @ModelAttribute("conversationReplyCreate") RequestParameters requestParameters,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext) {

        final ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {

            modelAndView.setViewName(
                    "com/wevserver/conversation/templates/conversation-reply-create");
            modelAndView.addObject("conversationReplyCreate", requestParameters);

            return modelAndView;
        }

        final ConversationReply conversationReply =
                conversationReplyRepository.save(
                        new ConversationReply(
                                UUID.randomUUID().toString(),
                                null,
                                null,
                                requestParameters.getName(),
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName()));

        redirectAttributes.addAttribute("id", conversationReply.getId());
        return new ModelAndView(
                "redirect:/conversation-conversation-reply/conversation-reply-read/{id}");
    }

    @Getter
    @Setter
    private static class RequestParameters {

        private String name;
    }
}
