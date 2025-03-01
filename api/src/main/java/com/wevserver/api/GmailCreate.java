package com.wevserver.api;

import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class GmailCreate {

    public static final String PATH = "/mail/gmail-create";

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestParams {

        @NotNull private String to;

        @NotNull private String subject;

        @NotNull private String content;

        public MultiValueMap<String, String> map() {

            final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

            if (Objects.nonNull(to)) {

                map.add("to", to);
            }

            if (Objects.nonNull(subject)) {

                map.add("subject", subject);
            }

            if (Objects.nonNull(content)) {

                map.add("content", content);
            }

            return map;
        }

        public RequestParams(final MultiValueMap<String, String> map) {

            to = map.getFirst("to");
            subject = map.getFirst("subject");
            subject = map.getFirst("content");
        }
    }
}
