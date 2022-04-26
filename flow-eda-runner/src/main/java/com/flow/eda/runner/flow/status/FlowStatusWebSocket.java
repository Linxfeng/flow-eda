package com.flow.eda.runner.flow.status;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** 用于向前端推送流程的状态信息 */
@Slf4j
@Component
@EqualsAndHashCode
@ServerEndpoint("/ws/flow/status/{id}")
public class FlowStatusWebSocket {
    /** 每个流程id对应一个session */
    private static final Map<String, Session> SESSION_POOL = new ConcurrentHashMap<>();

    @Autowired private FlowStatusMqService flowStatusMqService;

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        SESSION_POOL.put(id, session);
        log.info("New client connected, flow id:{}", id);
    }

    @OnClose
    public void onClose(@PathParam("id") String id) {
        SESSION_POOL.remove(id);
        log.info("Client disconnected, flow id:{}", id);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket session {} onError:{}", session.getId(), error.getMessage());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("Received client message:{}", message);
    }

    /** 推送流程状态信息 */
    public void sendMessage(String flowId, Document message) {
        if (SESSION_POOL.get(flowId) != null) {
            try {
                synchronized (SESSION_POOL.get(flowId)) {
                    SESSION_POOL.get(flowId).getBasicRemote().sendText(message.toJson());
                }
            } catch (Exception e) {
                log.error("Send websocket message failed:{}", e.getMessage());
            }
        }
    }
}
