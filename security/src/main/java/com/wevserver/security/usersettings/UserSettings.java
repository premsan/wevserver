package com.wevserver.security.usersettings;

import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@Setter
@NoArgsConstructor
public class UserSettings implements Serializable {

    private String language;

    private String timeZone;

    public MultiValueMap<String, String> map() {

        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        if (Objects.nonNull(language)) {

            map.add("language", language);
        }

        if (Objects.nonNull(timeZone)) {

            map.add("timeZone", timeZone);
        }

        return map;
    }

    public UserSettings(final MultiValueMap<String, String> map) {

        language = map.getFirst("language");
        timeZone = map.getFirst("timeZone");
    }
}
