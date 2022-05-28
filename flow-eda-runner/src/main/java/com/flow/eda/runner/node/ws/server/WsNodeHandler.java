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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/** 自定义WebSocketHandler，用于处理ws_server节点 */
@Slf4j
public class WsNodeHandler extends AbstractWebSocketHandler {
    /** 管理path路径和session的对应关系 */
    private static final Map<String, List<WebSocketSession>> SESSION_MAP =
            new ConcurrentHashMap<>();

    public WsNodeHandler() {
        super();
    }

    /** 向指定客户端session发送消息 */
    public static void sendMessage(WebSocketSession session, String message) {
        if (session != null) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                log.error("WsNodeHandler: Send websocket message failed:{}", e.getMessage());
            }
        }
    }

    /** 向指定path路径的所有客户端session发送消息 */
    public static void sendMessage(String path, String message) {
        if (SESSION_MAP.containsKey(path)) {
            SESSION_MAP.get(path).forEach(s -> sendMessage(s, message));
        }
    }

    /** 获取ws请求路径 */
    private String getPath(WebSocketSession session) {
        return Objects.requireNonNull(session.getUri()).getPath();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String path = getPath(session);
        this.addSession(session);
        log.info("WsNodeHandler: Client connected, path: {}", path);

        // 建立连接后发送消息
        WsServerNode node = WsServerNodeManager.getNodeInstance(path);
        Map<String, Object> kv = this.parseQuery(session, node.getQuery());
        sendMessageReplacePlaceholder(session, node.getSendAfterConnect(), kv);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        if (message instanceof TextMessage) {
            String payload = ((TextMessage) message).getPayload();
            log.info("WsNodeHandler: Received client message:{}", payload);

            // 收到消息后输出，向下游节点传递
            WsServerNode node = WsServerNodeManager.getNodeInstance(getPath(session));
            node.callback(payload);

            // 收到消息后发送消息
            Map<String, Object> kv = this.parseQuery(session, node.getQuery());
            sendMessageReplacePlaceholder(session, node.getSendAfterReceive(), kv);
        } else {
            throw new IllegalStateException("Unexpected WebSocket message type: " + message);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error(
                "WsNodeHandler: Websocket path: {} onError: {}",
                getPath(session),
                exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        this.removeSession(session);
        log.info(
                "WsNodeHandler: Client disconnected, path: {}, Reason: {}",
                getPath(session),
                status.getReason());
    }

    /** 发送指定消息，解析并替换占位符 */
    private void sendMessageReplacePlaceholder(
            WebSocketSession session, String payload, Map<String, Object> kv) {
        if (StringUtils.hasText(payload)) {
            Document message = new Document("k", payload);
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

    private void addSession(WebSocketSession session) {
        String path = getPath(session);
        if (SESSION_MAP.containsKey(path)) {
            SESSION_MAP.get(path).add(session);
        } else {
            List<WebSocketSession> list = new ArrayList<>();
            list.add(session);
            SESSION_MAP.put(path, list);
        }
    }

    private void removeSession(WebSocketSession session) {
        String path = getPath(session);
        if (SESSION_MAP.containsKey(path)) {
            SESSION_MAP.get(path).remove(session);
        }
    }
}
