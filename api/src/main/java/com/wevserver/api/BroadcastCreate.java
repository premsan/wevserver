package com.wevserver.api;

import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class BroadcastCreate {

    public static final String PATH = "/broadcast/broadcast-create";

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestParams {

        private String name;

        private String url;

        public MultiValueMap<String, String> map() {

            final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

            if (Objects.nonNull(name)) {

                map.add("name", name);
            }

            if (Objects.nonNull(url)) {

                map.add("url", url);
            }

            return map;
        }

        public RequestParams(final MultiValueMap<String, String> map) {

            name = map.getFirst("name");
            url = map.getFirst("url");
        }
    }
}
