package com.wevserver.api;

import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class FavouriteUriDelete {

    public static final String PATH = "/session/favourite-uri-delete";

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestParams {

        @NotBlank private String uri;

        @NotBlank private String redirectUri;

        public MultiValueMap<String, String> map() {

            final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

            if (Objects.nonNull(uri)) {

                map.add("uri", uri);
            }

            if (Objects.nonNull(redirectUri)) {

                map.add("redirectUri", redirectUri);
            }

            return map;
        }

        public RequestParams(final MultiValueMap<String, String> map) {

            uri = map.getFirst("uri");
            redirectUri = map.getFirst("redirectUri");
        }
    }
}
