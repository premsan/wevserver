package com.wevserver.application.propertypicker;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

public class PropertyPicked {

    private PropertyPicker propertyPicker;

    private Map<String, String> properties;
    private MultiValueMap<String, String> params;

    public PropertyPicked(final PropertyPicker propertyPicker) {

        this.propertyPicker = propertyPicker;

        if (properties == null) {

            properties = new HashMap<>();
            for (final String mapping : propertyPicker.getMapping()) {

                final String[] kv = mapping.split("\\.");
                properties.put(kv[0], kv[1]);
            }
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

    public String redirectUri() {

        return UriComponentsBuilder.fromPath(propertyPicker.getRedirectUri())
                .queryParams(params)
                .build()
                .toString();
    }
}
