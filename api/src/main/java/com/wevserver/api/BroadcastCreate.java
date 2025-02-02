package com.wevserver.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BroadcastCreate {

    public static final String PATH = "/broadcast/broadcast-create";

    @Getter
    @Setter
    public static class RequestParameters {

        private String reference;

        private String name;

        private String url;
    }
}
