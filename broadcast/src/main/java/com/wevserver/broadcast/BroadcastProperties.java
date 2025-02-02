package com.wevserver.broadcast;

import java.net.URI;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "com.wevserver.broadcast")
public class BroadcastProperties {

    private List<URI> servers;

    private List<URI> clients;
}
