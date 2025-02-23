package com.wevserver.application.propertypicker;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyPicker {

    private Set<String> mapping;
    private String redirectUri;
}
