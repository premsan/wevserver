package com.wevserver.application.selector;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectorRequest {

    private Map<String, String> requestParams;
    private String targetUri;
}