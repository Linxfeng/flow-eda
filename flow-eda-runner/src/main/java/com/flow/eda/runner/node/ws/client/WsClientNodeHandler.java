package com.flow.eda.runner.node.ws.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Objects;

/** 自定义WebSocketHandler，用于处理ws_client节点 */
@Slf4j
public class WsClientNodeHandler extends AbstractWebSocketHandler {

    private final WsClientNode node;

    public WsClientNodeHandler(WsClientNode node) {
        super();
        this.node = node;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String path = getPath(session);
        log.info("WsClientNodeHandler: websocket connected, path: {}", path);

        // 建立连接后发送消息
        String sendAfterConnect = node.getSendAfterConnect();
        if (StringUtils.hasText(sendAfterConnect)) {
            sendMessage(session, sendAfterConnect);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        if (message instanceof TextMessage) {
            String payload = ((TextMessage) message).getPayload();
            log.info("WsClientNodeHandler: Received message:{}", payload);

            // 收到消息后回调，向下游节点输出
            node.callback(payload);
        } else {
            throw new IllegalStateException("Unexpected WebSocket message type: " + message);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error(
                "WsClientNodeHandler: Websocket path: {} onError: {}",
                getPath(session),
                exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info(
                "WsClientNodeHandler: Websocket disconnected, path: {}, Reason: {}",
                getPath(session),
                status.getReason());
    }

    /** 获取ws请求路径 */
    private String getPath(WebSocketSession session) {
        return Objects.requireNonNull(session.getUri()).getPath();
    }

    /** 发送消息 */
    private void sendMessage(WebSocketSession session, String message) {
        if (session != null) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                log.error("WsServerNodeHandler: Send websocket message failed:{}", e.getMessage());
            }
        }
    }
}
