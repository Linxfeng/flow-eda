package com.flow.eda.runner.node.ws.server;

import com.flow.eda.common.utils.CollectionUtil;
import com.flow.eda.runner.utils.PlaceholderUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** 自定义WebSocketHandler，用于处理ws_server节点 */
@Slf4j
public class WsNodeHandler extends AbstractWebSocketHandler {

    public WsNodeHandler() {
        super();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String path = Objects.requireNonNull(session.getUri()).getPath();
        log.info("WsNodeHandler: Client connected, path: {}", path);

        // 建立连接后发送消息
        WsServerNode node = WsServerNodeManager.getNodeInstance(path);
        String sendAfterConnected = node.getSendAfterConnected();

        if (StringUtils.hasText(sendAfterConnected)) {
            Document message = new Document("k", sendAfterConnected);
            // 获取ws请求参数，解析并填充占位符
            Map<String, Object> kv = this.parseQuery(session, node.getQuery());
            if (!kv.isEmpty()) {
                message = PlaceholderUtil.replacePlaceholder(message, new Document(kv));
            }
            // 发送消息
            sendMessage(session, message.getString("k"));
        }
    }

    /** 解析ws请求参数的key-values */
    private Map<String, Object> parseQuery(WebSocketSession session, List<String> keys) {
        Map<String, Object> kv = new HashMap<>();
        if (CollectionUtil.isEmpty(keys)) {
            return kv;
        }
        String query = Objects.requireNonNull(session.getUri()).getQuery();
        if (!StringUtils.hasText(query)) {
            return kv;
        }
        for (String q : query.split("&")) {
            String[] split = q.split("=");
            if (keys.contains(split[0])) {
                kv.put(split[0], split[1]);
            }
        }
        return kv;
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

    public void sendMessage(WebSocketSession session, String message) {
        if (session != null) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                log.error("WsNodeHandler: Send websocket message failed:{}", e.getMessage());
            }
        }
    }
}
