package com.wevserver.broadcast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wevserver.broadcast.broadcast.Broadcast;
import com.wevserver.broadcast.broadcast.BroadcastRepository;
import com.wevserver.broadcast.broadcastserver.BroadcastServer;
import com.wevserver.broadcast.broadcastserver.BroadcastServerRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Service
@RequiredArgsConstructor
public class BroadcastSubscriber extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final BroadcastRepository broadcastRepository;

    private final BroadcastServerRepository broadcastServerRepository;
    private final BroadcastPublisher broadcastPublisher;

    private boolean open = false;

    private Map<String, WebSocketConnectionManager> connectionManagers = new ConcurrentHashMap<>();

    @Override
    public void handleTextMessage(final WebSocketSession session, final TextMessage message) {

        final Broadcast broadcast;
        try {
            broadcast = objectMapper.readValue(message.getPayload(), Broadcast.class);

            if (!broadcastRepository.existsById(broadcast.getId())) {

                broadcastRepository.save(broadcast);
            }

            broadcastPublisher.sendMessage(message);

        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void openConnection() {

        open = true;

        for (final BroadcastServer broadcastServer : broadcastServerRepository.findAll()) {

            if (broadcastServer.getEnabled()) {

                final WebSocketConnectionManager webSocketConnectionManager =
                        new WebSocketConnectionManager(
                                new StandardWebSocketClient(), this, broadcastServer.getUrl());

                connectionManagers.put(broadcastServer.getId(), webSocketConnectionManager);

                webSocketConnectionManager
                        .getHeaders()
                        .setBasicAuth(broadcastServer.getUsername(), broadcastServer.getPassword());

                try {

                    webSocketConnectionManager.start();

                } catch (final RuntimeException exception) {

                }
            }
        }
    }

    public void closeConnection() {

        open = false;

        for (final WebSocketConnectionManager webSocketConnectionManager :
                connectionManagers.values()) {

            webSocketConnectionManager.stop();
        }
    }

    public boolean isOpen() {

        return open;
    }
}
