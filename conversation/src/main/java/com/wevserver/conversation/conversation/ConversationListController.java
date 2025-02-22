package com.wevserver.conversation.conversation;

import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.application.selector.SelectorRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @GetMapping("/conversation/conversation-list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CONVERSATION_LIST')")
    public ModelAndView conversationListGet(
            final HttpSession httpSession, final RequestParams requestParams) {

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

        httpSession.getAttribute("ok");

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/conversation/templates/conversation-list");

        modelAndView.addObject("conversationPage", conversationPage);

        return modelAndView;
    }

    @Getter
    @Setter
    private static class RequestParams {

        private String idEquals;

        private String nameStartingWith;

        @Min(0)
        private Integer pageNumber = 0;

        @Min(0)
        @Max(1024)
        private Integer pageSize = 32;

        private SortBy sortBy;

        private SelectorRequest selectorRequest;

        private enum SortBy {
            UPDATED_AT_ASC(Sort.by(Sort.Direction.ASC, "updatedAt")),
            UPDATED_AT_DESC(Sort.by(Sort.Direction.DESC, "updatedAt"));

            private final Sort sort;

            SortBy(final Sort sort) {
                this.sort = sort;
            }
        }

        private Pageable getPageable() {

            final Sort sort = sortBy == null ? Sort.unsorted() : sortBy.sort;

            return PageRequest.of(pageNumber, pageSize, sort);
        }
    }

    @Getter
    @Setter
    private static class ConversationListItem {

        private String id;

        private Long version;

        private String name;

        private Long updatedAt;

        private String updatedBy;

        private SelectorRequest selectorRequest;

        public ConversationListItem(final Conversation conversation) {

            this.id = conversation.getId();
            this.version = conversation.getVersion();
            this.name = conversation.getName();
            this.updatedAt = conversation.getUpdatedAt();
            this.updatedBy = conversation.getUpdatedBy();
        }
    }
}
