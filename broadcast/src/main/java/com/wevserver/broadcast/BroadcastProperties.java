package com.wevserver.broadcast;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "com.wevserver.broadcast")
public class BroadcastProperties {

    private boolean inboundAutoStartup;

    private boolean outboundAutoStartup;
}
