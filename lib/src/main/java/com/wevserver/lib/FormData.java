package com.wevserver.lib;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

@Getter
@Setter
@AllArgsConstructor
public class FormData {

    private MultiValueMap<String, Object> data;

    public FormData(final Map<String, Object> map) {

        this.data = new LinkedMultiValueMap<>();

        map.forEach((key, value) -> this.data.add(key, value));
    }

    public String encode() {

        final StringBuilder builder = new StringBuilder();

        data.forEach(
                (name, values) -> {
                    if (name == null) {
                        return;
                    }
                    values.forEach(
                            value -> {
                                if (builder.length() != 0) {
                                    builder.append('&');
                                }
                                builder.append(URLEncoder.encode(name, StandardCharsets.UTF_8));
                                if (value != null) {
                                    builder.append('=');
                                    builder.append(
                                            URLEncoder.encode(
                                                    String.valueOf(value), StandardCharsets.UTF_8));
                                }
                            });
                });

        return builder.toString();
    }

    public static FormData decode(final String encoded) {

        final String[] pairs = StringUtils.tokenizeToStringArray(encoded, "&");

        final MultiValueMap<String, Object> map = new LinkedMultiValueMap<>(pairs.length);

        for (final String pair : pairs) {
            final int idx = pair.indexOf('=');
            if (idx == -1) {
                map.add(URLDecoder.decode(pair, StandardCharsets.UTF_8), null);
            } else {
                final String name =
                        URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                final String value =
                        URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                map.add(name, value);
            }
        }

        return new FormData(map);
    }
}
