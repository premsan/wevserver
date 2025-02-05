package com.wevserver.broadcast;

import com.wevserver.api.BroadcastWebsocket;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class BroadcastConfiguration implements WebSocketConfigurer {

    private final BroadcastServerManager broadcastServerManager;

    @Override
    public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
        registry.addHandler(broadcastServerManager, BroadcastWebsocket.PATH);
    }
}
