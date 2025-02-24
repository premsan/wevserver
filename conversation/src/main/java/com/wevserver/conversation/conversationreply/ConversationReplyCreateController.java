package com.wevserver.conversation.conversationreply;

import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.conversation.conversation.ConversationRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_REPLY_CREATE')")
    public ModelAndView conversationReplyCreateGet(
            final HttpSession httpSession, final RequestParams requestParams) {

        final MultiValueMap<String, String> sessionData =
                (MultiValueMap<String, String>)
                        httpSession.getAttribute("/conversation/conversation-reply-create");

        if (Objects.nonNull(sessionData)) {

            requestParams.merge(sessionData);
        }

        final ModelAndView model =
                new ModelAndView("com/wevserver/conversation/templates/conversation-reply-create");

        model.addObject("requestParams", requestParams);

        return model;
    }

    @PostMapping("/conversation/conversation-reply-create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_REPLY_CREATE')")
    public ModelAndView conversationReplyCreatePost(
            @Valid RequestParams requestParams,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext) {

        final ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {

            modelAndView.setViewName(
                    "com/wevserver/conversation/templates/conversation-reply-create");
            modelAndView.addObject("requestParams", requestParams);

            return modelAndView;
        }

        if (!conversationRepository.existsById(requestParams.getConversationId())) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final ConversationReply conversationReply =
                conversationReplyRepository.save(
                        new ConversationReply(
                                UUID.randomUUID().toString(),
                                null,
                                requestParams.getConversationId(),
                                requestParams.getDescription(),
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName()));

        redirectAttributes.addAttribute("id", conversationReply.getId());
        return new ModelAndView("redirect:/conversation/conversation-reply-read/{id}");
    }

    @Getter
    @Setter
    private static class RequestParams {

        @NotNull private String conversationId;

        @NotBlank private String description;

        public void merge(final MultiValueMap<String, String> multiValueMap) {

            if (Objects.isNull(conversationId)) {

                this.conversationId = multiValueMap.getFirst("conversationId");
            }

            if (Objects.isNull(description)) {

                this.description = multiValueMap.getFirst("description");
            }
        }
    }
}
