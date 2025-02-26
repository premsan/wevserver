package com.wevserver.api;

import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class SessionDataCreate {

    public static final String PATH = "/session/data-create";

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestParams {

        @NotBlank private String redirectUri;

        @NotBlank private String dataId;

        private MultiValueMap<String, String> data;

        public MultiValueMap<String, String> map() {

            final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

            if (Objects.nonNull(dataId)) {

                map.add("_dataId", dataId);
            }

            if (Objects.nonNull(redirectUri)) {

                map.add("_redirectUri", redirectUri);
            }

            if (Objects.nonNull(data)) {

                map.addAll(data);
            }

            return map;
        }

        public RequestParams(final MultiValueMap<String, String> map) {

            dataId = map.getFirst("_dataId");
            redirectUri = map.getFirst("_redirectUri");

            data = new LinkedMultiValueMap<>(map);

            data.remove("_dataId");
            data.remove("_redirectUri");
        }
    }
}
