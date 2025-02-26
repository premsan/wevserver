package com.wevserver.api;

import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class ConversationReplyCreate {

    public static final String PATH = "/conversation/conversation-reply-create";

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestParams {

        @NotBlank private String conversationId;

        @NotBlank private String description;

        public MultiValueMap<String, String> map() {

            final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

            if (Objects.nonNull(conversationId)) {

                map.add("conversationId", conversationId);
            }

            if (Objects.nonNull(description)) {

                map.add("description", description);
            }

            return map;
        }

        public RequestParams(final MultiValueMap<String, String> map) {

            conversationId = map.getFirst("conversationId");
            description = map.getFirst("description");
        }
    }
}
