package com.wevserver.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

public class ConversationList {

    public static final String PATH = "/conversation/conversation-list";

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestParams {

        private String idEquals;

        private String nameStartingWith;

        @Min(0)
        private Integer pageNumber = 0;

        @Min(0)
        @Max(1024)
        private Integer pageSize = 32;

        private SortBy sortBy;

        private PropertyPick.RequestParams propertyPickRequestParams;

        @Getter
        private enum SortBy {
            UPDATED_AT_ASC(Sort.by(Sort.Direction.ASC, "updatedAt")),
            UPDATED_AT_DESC(Sort.by(Sort.Direction.DESC, "updatedAt"));

            private final Sort sort;

            SortBy(final Sort sort) {
                this.sort = sort;
            }
        }

        public Pageable getPageable() {

            final Sort sort = sortBy == null ? Sort.unsorted() : sortBy.getSort();

            return PageRequest.of(pageNumber, pageSize, sort);
        }

        public MultiValueMap<String, String> map() {

            final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

            if (StringUtils.hasText(idEquals)) {

                map.add("idEquals", idEquals);
            }

            if (StringUtils.hasText(nameStartingWith)) {

                map.add("nameStartingWith", nameStartingWith);
            }

            if (Objects.nonNull(pageNumber)) {

                map.add("pageNumber", String.valueOf(pageNumber));
            }

            if (Objects.nonNull(pageSize)) {

                map.add("pageSize", String.valueOf(pageSize));
            }

            if (Objects.nonNull(sortBy)) {

                map.add("sortBy", String.valueOf(sortBy));
            }

            return map;
        }
    }
}
