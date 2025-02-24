package com.wevserver.conversation.conversation;

import com.wevserver.api.ConversationCreate;
import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.ui.ErrorMessagesSupplier;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
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
    @GetMapping(ConversationCreate.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_CREATE')")
    public ModelAndView conversationCreateGet(
            final HttpSession httpSession, final ConversationCreate.RequestParams requestParams) {

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        final MultiValueMap<String, String> savedParams =
                (MultiValueMap<String, String>) httpSession.getAttribute(ConversationCreate.PATH);

        if (Objects.nonNull(savedParams)) {

            params.putAll(savedParams);
        }

        params.putAll(requestParams.map());

        final ModelAndView model =
                new ModelAndView("com/wevserver/conversation/templates/conversation-create");

        model.addObject("requestParams", new ConversationCreate.RequestParams(params));

        return model;
    }

    @PostMapping(ConversationCreate.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_CREATE')")
    public ModelAndView conversationCreatePost(
            @Valid final ConversationCreate.RequestParams requestParams,
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
}
