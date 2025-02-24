package com.wevserver.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

public class URLEncodedUtils {

    public static String encode(final MultiValueMap<String, String> map, final Charset charset) {

        final StringBuilder builder = new StringBuilder();

        map.forEach(
                (name, values) -> {
                    if (name == null) {
                        return;
                    }
                    values.forEach(
                            value -> {
                                if (builder.length() != 0) {
                                    builder.append('&');
                                }
                                builder.append(URLEncoder.encode(name, charset));
                                if (value != null) {
                                    builder.append('=');
                                    builder.append(URLEncoder.encode(value, charset));
                                }
                            });
                });

        return builder.toString();
    }

    public static MultiValueMap<String, String> decode(
            final String encoded, final Charset charset) {

        final String[] pairs = StringUtils.tokenizeToStringArray(encoded, "&");

        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>(pairs.length);

        for (final String pair : pairs) {
            final int idx = pair.indexOf('=');
            if (idx == -1) {
                map.add(URLDecoder.decode(pair, charset), null);
            } else {
                final String name = URLDecoder.decode(pair.substring(0, idx), charset);
                final String value = URLDecoder.decode(pair.substring(idx + 1), charset);
                map.add(name, value);
            }
        }

        return map;
    }
}
