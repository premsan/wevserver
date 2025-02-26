package com.wevserver.api;

import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class ConversationCreate {

    public static final String PATH = "/conversation/conversation-create";

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestParams {

        @NotBlank private String name;

        public MultiValueMap<String, String> map() {

            final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

            if (Objects.nonNull(name)) {

                map.add("name", name);
            }

            return map;
        }

        public RequestParams(final MultiValueMap<String, String> map) {

            name = map.getFirst("name");
        }
    }
}
