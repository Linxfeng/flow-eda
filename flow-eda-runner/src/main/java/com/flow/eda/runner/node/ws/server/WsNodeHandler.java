package com.flow.eda.runner.node.ws.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.websocket.Session;
import java.util.Objects;

/** 自定义WebSocketHandler，用于处理ws_server节点 */
@Slf4j
public class WsNodeHandler extends AbstractWebSocketHandler {

    public WsNodeHandler() {
        super();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String path = Objects.requireNonNull(session.getUri()).getPath();
        log.info("Client connected WebSocketHandler, path: {}", path);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
            throws Exception {
        super.handleMessage(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception)
            throws Exception {
        log.error("websocket session {} onError:{}", session.getId(), exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
            throws Exception {
        log.info(
                "Client disconnected, session id:{}, CloseStatus:{}",
                session.getId(),
                status.getCode());
    }

    public void sendMessage(Session session, String message) {
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error("Send websocket message failed:{}", e.getMessage());
            }
        }
    }
}
