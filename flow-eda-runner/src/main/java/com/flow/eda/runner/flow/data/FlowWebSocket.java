package com.flow.eda.runner.flow.data;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Service
@EqualsAndHashCode
@ServerEndpoint("/ws/flow/{id}/nodes")
public class FlowWebSocket {
    private static final CopyOnWriteArraySet<FlowWebSocket> WEBSOCKETS =
            new CopyOnWriteArraySet<>();
    /** 每个流程id对应一个session */
    private static final Map<String, Session> SESSION_POOL = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        WEBSOCKETS.add(this);
        SESSION_POOL.put(id, session);
        log.info("New client id:{} connected, the current total clients:{}", id, WEBSOCKETS.size());
    }

    @OnClose
    public void onClose() {
        WEBSOCKETS.remove(this);
        log.info("Client disconnected, the current total clients:{}", WEBSOCKETS.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("Received client message:{}", message);
    }

    public void sendMessage(String flowId, String message) {
        if (SESSION_POOL.get(flowId) == null) {
            return;
        }
        try {
            synchronized (SESSION_POOL.get(flowId)) {
                SESSION_POOL.get(flowId).getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("Send websocket message failed:{}", e.getMessage());
        }
    }
}
