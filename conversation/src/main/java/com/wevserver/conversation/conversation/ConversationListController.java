package com.wevserver.conversation.conversation;

import com.wevserver.api.ConversationList;
import com.wevserver.api.PropertyPick;
import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.application.feature.FeatureType;
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

    @FeatureMapping(type = FeatureType.ENTITY_LIST, entity = Conversation.class)
    @GetMapping(ConversationList.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_LIST')")
    public ModelAndView conversationListGet(final ConversationList.RequestParams requestParams) {

        Page<Conversation> conversationPage;

        if (StringUtils.hasText(requestParams.getIdEquals())) {

            final List<Conversation> conversationList = new ArrayList<>();

            final Optional<Conversation> conversationOptional =
                    conversationRepository.findById(requestParams.getIdEquals());

            conversationOptional.ifPresent(conversationList::add);

            conversationPage = new PageImpl<>(conversationList);

        } else if (StringUtils.hasText(requestParams.getNameStartingWith())) {

            conversationPage =
                    conversationRepository.findByNameStartingWith(
                            requestParams.getNameStartingWith(), requestParams.getPageable());
        } else {

            conversationPage = conversationRepository.findAll(requestParams.getPageable());
        }

        return modelAndView(requestParams, conversationPage);
    }

    private ModelAndView modelAndView(
            final ConversationList.RequestParams requestParams,
            final Page<Conversation> conversationPage) {

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/conversation/templates/conversation-list");

        final Page<ConversationListItem> conversationListItemPage =
                conversationPage.map(
                        conversation ->
                                new ConversationListItem(
                                        conversation, requestParams.getPropertyPick()));

        modelAndView.addObject("conversationPage", conversationListItemPage);

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

        private String propertyPickRedirectUri;

        public ConversationListItem(
                final Conversation conversation,
                final PropertyPick.RequestParams propertyPickRequestParams) {

            this.id = conversation.getId();
            this.version = conversation.getVersion();
            this.name = conversation.getName();
            this.updatedAt = conversation.getUpdatedAt();
            this.updatedBy = conversation.getUpdatedBy();

            if (Objects.nonNull(propertyPickRequestParams)) {

                propertyPick(conversation, propertyPickRequestParams);
            }
        }

        private void propertyPick(
                final Conversation conversation, final PropertyPick.RequestParams requestParams) {

            final PropertyPick.ResponseParams responseParams =
                    new PropertyPick.ResponseParams(requestParams);

            responseParams.addProperty("id", conversation.getId());
            responseParams.addProperty("version", String.valueOf(conversation.getVersion()));
            responseParams.addProperty("name", String.valueOf(conversation.getName()));
            responseParams.addProperty("updatedAt", String.valueOf(conversation.getUpdatedAt()));
            responseParams.addProperty("updatedBy", String.valueOf(conversation.getUpdatedBy()));

            this.propertyPickRedirectUri = responseParams.buildRedirectUri();
        }
    }
}
