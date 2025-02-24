package com.wevserver.conversation.conversation;

import com.wevserver.api.ConversationList;
import com.wevserver.api.PropertyPick;
import com.wevserver.application.feature.FeatureMapping;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class ConversationListController {

    private final ConversationRepository conversationRepository;

    @FeatureMapping
    @GetMapping(ConversationList.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_LIST')")
    public ModelAndView conversationListGet(final ConversationList.RequestParams requestParams) {

        Page<ConversationListItem> conversationPage;

        if (StringUtils.hasText(requestParams.getIdEquals())) {

            final List<ConversationListItem> conversationList = new ArrayList<>();

            final Optional<Conversation> conversationOptional =
                    conversationRepository.findById(requestParams.getIdEquals());

            if (conversationOptional.isEmpty()) {

                conversationList.add(new ConversationListItem(conversationOptional.get()));
            }

            conversationPage = new PageImpl<>(conversationList);

        } else if (StringUtils.hasText(requestParams.getNameStartingWith())) {

            conversationPage =
                    conversationRepository
                            .findByNameStartingWith(
                                    requestParams.getNameStartingWith(),
                                    requestParams.getPageable())
                            .map(ConversationListItem::new);
        } else {

            conversationPage =
                    conversationRepository
                            .findAll(requestParams.getPageable())
                            .map(ConversationListItem::new);
        }

        if (Objects.nonNull(requestParams.getPropertyPickRequestParams())) {

            conversationPage =
                    conversationPage.map(
                            conversationListItem ->
                                    conversationListItem.propertyPickResponse(
                                            requestParams.getPropertyPickRequestParams()));
        }

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/conversation/templates/conversation-list");

        modelAndView.addObject("conversationPage", conversationPage);

        return modelAndView;
    }

    @Getter
    @Setter
    private static class ConversationListItem {

        private String id;

        private Long version;

        private String name;

        private Long updatedAt;

        private String updatedBy;

        private String propertyPickedRedirectUri;

        public ConversationListItem(final Conversation conversation) {

            this.id = conversation.getId();
            this.version = conversation.getVersion();
            this.name = conversation.getName();
            this.updatedAt = conversation.getUpdatedAt();
            this.updatedBy = conversation.getUpdatedBy();
        }

        public ConversationListItem propertyPickResponse(final PropertyPick.RequestParams requestParams) {

            final PropertyPick.ResponseParams responseParams =
                    new PropertyPick.ResponseParams(requestParams);

            responseParams.addProperty("id", this.id);
            responseParams.addProperty("version", String.valueOf(this.version));
            responseParams.addProperty("name", String.valueOf(name));
            responseParams.addProperty("updatedAt", String.valueOf(updatedAt));
            responseParams.addProperty("updatedBy", String.valueOf(updatedBy));

            this.propertyPickedRedirectUri = responseParams.redirectUri();

            return this;
        }
    }
}
