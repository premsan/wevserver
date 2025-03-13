package com.wevserver.conversation.conversationreply;

import com.wevserver.api.ConversationList;
import com.wevserver.api.ConversationReplyCreate;
import com.wevserver.api.PropertyPick;
import com.wevserver.api.SessionDataCreate;
import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.application.feature.FeatureType;
import com.wevserver.conversation.conversation.Conversation;
import com.wevserver.conversation.conversation.ConversationRepository;
import com.wevserver.ui.ErrorMessages;
import com.wevserver.ui.ErrorMessagesSupplier;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequiredArgsConstructor
public class ConversationReplyCreateController {

    private final ErrorMessagesSupplier errorMessagesSupplier;
    private final ConversationRepository conversationRepository;
    private final ConversationReplyRepository conversationReplyRepository;

    @FeatureMapping(type = FeatureType.ENTITY_CREATE, entity = ConversationReply.class)
    @GetMapping(ConversationReplyCreate.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_REPLY_CREATE')")
    public ModelAndView conversationReplyCreateGet(
            final HttpSession httpSession,
            final ConversationReplyCreate.RequestParams requestParams) {

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        final MultiValueMap<String, String> savedParams =
                (MultiValueMap<String, String>)
                        httpSession.getAttribute(ConversationReplyCreate.PATH);

        if (Objects.nonNull(savedParams)) {

            params.putAll(savedParams);
        }

        params.putAll(requestParams.map());

        return modelAndView(new ConversationReplyCreate.RequestParams(params), null);
    }

    @PostMapping(ConversationReplyCreate.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_REPLY_CREATE')")
    public ModelAndView conversationReplyCreatePost(
            @Valid ConversationReplyCreate.RequestParams requestParams,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext) {

        if (bindingResult.hasErrors()) {

            return modelAndView(requestParams, errorMessagesSupplier.get(bindingResult));
        }

        final Optional<Conversation> conversationOptional =
                conversationRepository.findById(requestParams.getConversationId());

        if (conversationOptional.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final ConversationReply conversationReply =
                conversationReplyRepository.save(
                        new ConversationReply(
                                UUID.randomUUID().toString(),
                                null,
                                conversationOptional.get().getId(),
                                requestParams.getDetails(),
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName(),
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName()));

        redirectAttributes.addAttribute("id", conversationReply.getId());
        return new ModelAndView("redirect:/conversation/conversation-reply-read/{id}");
    }

    private ModelAndView modelAndView(
            final ConversationReplyCreate.RequestParams requestParams,
            final ErrorMessages errorMessages) {

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/conversation/templates/conversation-reply-create");

        modelAndView.addObject("conversationId", requestParams.getConversationId());
        modelAndView.addObject("details", requestParams.getDetails());

        final PropertyPick.RequestParams propertyPickRequestParams =
                new PropertyPick.RequestParams();
        propertyPickRequestParams.setRedirectUri(ConversationReplyCreate.PATH);
        propertyPickRequestParams.setMapping(Set.of("id.conversationId"));

        final ConversationList.RequestParams conversationListRequestParams =
                new ConversationList.RequestParams();
        conversationListRequestParams.setPropertyPick(propertyPickRequestParams);

        final SessionDataCreate.RequestParams afterSaveConversationPickParams =
                new SessionDataCreate.RequestParams();
        afterSaveConversationPickParams.setDataId(ConversationReplyCreate.PATH);
        afterSaveConversationPickParams.setRedirectUri(
                UriComponentsBuilder.fromPath(ConversationList.PATH)
                        .queryParams(conversationListRequestParams.map())
                        .encode()
                        .build()
                        .toUriString());

        modelAndView.addObject(
                "pickConversationUri",
                UriComponentsBuilder.fromPath(SessionDataCreate.PATH)
                        .queryParams(afterSaveConversationPickParams.map())
                        .encode()
                        .build()
                        .toString());

        final SessionDataCreate.RequestParams saveRequestParams =
                new SessionDataCreate.RequestParams();
        saveRequestParams.setDataId(ConversationReplyCreate.PATH);
        saveRequestParams.setRedirectUri(ConversationReplyCreate.PATH);

        modelAndView.addObject(
                "saveUri",
                UriComponentsBuilder.fromPath(SessionDataCreate.PATH)
                        .queryParams(saveRequestParams.map())
                        .encode()
                        .build()
                        .toString());

        if (Objects.nonNull(errorMessages)) {

            modelAndView.addObject("fieldErrors", errorMessages.getFieldErrors());
            modelAndView.addObject("globalErrors", errorMessages.getGlobalErrors());
        }

        return modelAndView;
    }
}
