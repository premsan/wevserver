package com.wevserver.broadcast;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Service
@RequiredArgsConstructor
public class BroadcastServerManager extends TextWebSocketHandler {

    private static final int WAIT_RESPONSE_MAX_MS = 10_000;
    private static final int MAX_MESSAGE_BUFFER_SIZE = 10 * 1024 * 1024;

    private Map<String, WebSocketSession> sessionByUsername = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {

        final String username = session.getPrincipal().getName();

        final WebSocketSession sessionWrapper =
                new ConcurrentWebSocketSessionDecorator(
                        session, WAIT_RESPONSE_MAX_MS, MAX_MESSAGE_BUFFER_SIZE);
        final WebSocketSession previousSession = sessionByUsername.put(username, sessionWrapper);

        if (Objects.nonNull(previousSession)) {

            previousSession.close();
        }

        super.afterConnectionEstablished(sessionWrapper);
    }

    public void sendMessage(final WebSocketMessage<?> message) {

        for (final WebSocketSession session : sessionByUsername.values()) {

            try {

                session.sendMessage(message);

            } catch (final IOException e) {

            }
        }
    }
}
