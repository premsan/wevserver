package com.wevserver.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class PollCreate {

    public static final String PATH = "/poll/poll-create";

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestParams {

        @NotBlank private String name;

        @NotBlank private String details;

        @NotNull private ZoneId timeZone;

        @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime startAt;

        @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime endAt;

        public MultiValueMap<String, String> map() {

            final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

            if (Objects.nonNull(name)) {

                map.add("name", name);
            }

            if (Objects.nonNull(details)) {

                map.add("details", details);
            }

            if (Objects.nonNull(timeZone)) {

                map.add("timeZoneId", String.valueOf(timeZone));
            }

            if (Objects.nonNull(startAt)) {

                map.add("startAt", String.valueOf(startAt));
            }

            if (Objects.nonNull(endAt)) {

                map.add("endAt", String.valueOf(endAt));
            }

            return map;
        }

        public RequestParams(final MultiValueMap<String, String> map) {

            name = map.getFirst("name");
            details = map.getFirst("details");

            if (Objects.nonNull(map.getFirst("timeZoneId"))) {

                timeZone = ZoneId.of(map.getFirst("timeZoneId"));
            }

            if (Objects.nonNull(map.getFirst("startAt"))) {

                startAt = LocalDateTime.parse(map.getFirst("startAt"));
            }

            if (Objects.nonNull(map.getFirst("endAt"))) {

                endAt = LocalDateTime.parse(map.getFirst("endAt"));
            }
        }
    }
}
