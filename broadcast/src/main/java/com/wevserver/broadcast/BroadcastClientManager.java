package com.wevserver.broadcast;

import com.wevserver.broadcast.broadcastserver.BroadcastServer;
import com.wevserver.broadcast.broadcastserver.BroadcastServerRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Service
@RequiredArgsConstructor
public class BroadcastClientManager extends TextWebSocketHandler {

    private final BroadcastServerRepository broadcastServerRepository;
    private final BroadcastServerManager broadcastServerManager;

    private boolean open = false;

    private Map<String, WebSocketConnectionManager> connectionManagerByBroadcastServerId =
            new ConcurrentHashMap<>();

    @Override
    public void handleMessage(final WebSocketSession session, final WebSocketMessage<?> message) {

        broadcastServerManager.sendMessage(message);
    }

    public void openConnection() {

        open = true;

        for (final BroadcastServer broadcastServer : broadcastServerRepository.findAll()) {

            if (broadcastServer.getEnabled()) {

                final WebSocketConnectionManager webSocketConnectionManager =
                        new WebSocketConnectionManager(
                                new StandardWebSocketClient(), this, broadcastServer.getUrl());

                connectionManagerByBroadcastServerId.put(
                        broadcastServer.getId(), webSocketConnectionManager);

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
                connectionManagerByBroadcastServerId.values()) {

            webSocketConnectionManager.stop();
        }
    }

    public boolean isOpen() {

        return open;
    }
}
