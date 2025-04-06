package com.wevserver.api;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class PollOptionCreate {

    public static final String PATH = "/poll/poll-option-create";

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestParams {

        private Integer size = 0;

        @NotEmpty private List<String> name;

        public MultiValueMap<String, String> map() {

            final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

            if (Objects.nonNull(name)) {

                map.addAll("name", name);
            }

            return map;
        }

        public RequestParams(final MultiValueMap<String, String> map) {

            name = map.get("name");
        }
    }
}
