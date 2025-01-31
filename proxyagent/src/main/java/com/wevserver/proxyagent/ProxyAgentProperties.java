package com.wevserver.proxyagent;

import java.net.URI;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "com.wevserver.proxyagent")
public class ProxyAgentProperties {

    private URI server;

    private URI websocketUriOverride;

    private String username;

    private String password;
}
