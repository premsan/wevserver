package com.wevserver.conversation.conversation;

import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.ui.ErrorMessagesSupplier;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class ConversationCreateController {

    private final ErrorMessagesSupplier errorMessagesSupplier;
    private final ConversationRepository conversationRepository;

    @FeatureMapping
    @GetMapping("/conversation/conversation-create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_CREATE')")
    public ModelAndView conversationCreateGet(
            final HttpSession httpSession, final RequestParams requestParams) {

        final MultiValueMap<String, String> sessionData =
                (MultiValueMap<String, String>)
                        httpSession.getAttribute("/conversation/conversation-create");

        if (Objects.nonNull(sessionData)) {

            requestParams.merge(sessionData);
        }

        final ModelAndView model =
                new ModelAndView("com/wevserver/conversation/templates/conversation-create");

        model.addObject("requestParams", requestParams);

        return model;
    }

    @PostMapping("/conversation/conversation-create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_CREATE')")
    public ModelAndView conversationCreatePost(
            @Valid final RequestParams requestParams,
            final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext) {

        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {

            modelAndView.setViewName("com/wevserver/conversation/templates/conversation-create");

            modelAndView.addObject("requestParams", requestParams);
            modelAndView.addObject(
                    "errorMessages", errorMessagesSupplier.getErrorMessages(bindingResult));

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
        return new ModelAndView("redirect:/conversation/conversation-read/{id}");
    }

    @Getter
    @Setter
    private static class RequestParams {

        @NotBlank private String name;

        public void merge(final MultiValueMap<String, String> multiValueMap) {

            if (Objects.isNull(name)) {

                this.name = multiValueMap.getFirst("name");
            }
        }
    }
}
