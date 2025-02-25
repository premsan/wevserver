package com.wevserver.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Setter
public class PropertyPick {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestParams {

        private Set<String> mapping;
        private String redirectUri;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ResponseParams {

        private String redirectUri;

        private Map<String, String> properties;
        private MultiValueMap<String, String> params;

        public ResponseParams(final RequestParams requestParams) {

            redirectUri = requestParams.getRedirectUri();

            properties = new HashMap<>();

            for (final String mapping : requestParams.getMapping()) {

                final String[] kv = mapping.split("\\.");
                properties.put(kv[0], kv[1]);
            }
        }

        public void addProperty(final String name, final String value) {

            if (params == null) {

                params = new LinkedMultiValueMap<>();
            }
            if (properties.containsKey(name)) {

                params.add(properties.get(name), value);
            }
        }

        public String buildRedirectUri() {

            return UriComponentsBuilder.fromPath(redirectUri)
                    .queryParams(params)
                    .encode()
                    .build()
                    .toString();
        }
    }
}
