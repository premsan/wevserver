package com.wevserver.api;

import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class EmailCreate {

    public static final String PATH = "/email/email-create";

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestParams {

        @NotNull private String to;

        @NotNull private String subject;

        @NotNull private String body;

        @NotNull private String provider;

        public MultiValueMap<String, String> map() {

            final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

            if (Objects.nonNull(to)) {

                map.add("to", to);
            }

            if (Objects.nonNull(subject)) {

                map.add("subject", subject);
            }

            if (Objects.nonNull(body)) {

                map.add("body", body);
            }

            if (Objects.nonNull(provider)) {

                map.add("provider", provider);
            }

            return map;
        }

        public RequestParams(final MultiValueMap<String, String> map) {

            to = map.getFirst("to");
            subject = map.getFirst("subject");
            body = map.getFirst("body");
            provider = map.getFirst("provider");
        }
    }
}
