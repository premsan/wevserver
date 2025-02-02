package com.wevserver.broadcast;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Service
@RequiredArgsConstructor
public class BroadcastInboundHandler extends TextWebSocketHandler {

    private final BroadcastOutboundHandler broadcastOutboundHandler;

    @Override
    public void handleMessage(final WebSocketSession session, final WebSocketMessage<?> message) {

        broadcastOutboundHandler.sendMessage(message);
    }
}
