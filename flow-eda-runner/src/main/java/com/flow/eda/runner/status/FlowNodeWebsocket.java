package com.flow.eda.runner.status;

import com.flow.eda.runner.node.Node;
import com.flow.eda.runner.utils.FlowLogs;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** 用于向前端推送节点运行状态信息 */
@Slf4j
@Component
@EqualsAndHashCode
@ServerEndpoint("/ws/flow/{id}/nodes")
public class FlowNodeWebsocket {
    /** 每个流程id对应一个session */
    private static final Map<String, Session> SESSION_POOL = new ConcurrentHashMap<>();

    private static ApplicationContext applicationContext;

    /** 解决多例websocket实例下注入的bean为null的问题 */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        FlowNodeWebsocket.applicationContext = applicationContext;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        SESSION_POOL.put(id, session);
        log.info("New client connected, flow id:{}", id);
        // 立即推送一次当前流程状态
        Document message = new Document();
        String flowStatus =
                applicationContext.getBean(FlowStatusService.class).getFlowStatus(id, message);
        message.append("flowStatus", flowStatus);
        try {
            session.getBasicRemote().sendText(message.toJson());
        } catch (Exception e) {
            log.error("Send websocket message failed:{}", e.getMessage());
        }
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

    /** 推送节点状态和输出信息 */
    public void sendMessage(String flowId, Document message) {
        // 获取流程实时状态信息一起推送
        String flowStatus =
                applicationContext.getBean(FlowStatusService.class).getFlowStatus(flowId, message);
        message.append("flowStatus", flowStatus);
        if (SESSION_POOL.get(flowId) != null) {
            try {
                synchronized (SESSION_POOL.get(flowId)) {
                    SESSION_POOL.get(flowId).getBasicRemote().sendText(message.toJson());
                }
            } catch (Exception e) {
                log.error("Send websocket message failed:{}", e.getMessage());
            }
        }
        // 向mq中推送流程的实时运行状态信息
        applicationContext.getBean(FlowStatusMqProducer.class).sendFlowStatus(flowId, flowStatus);
        if (Node.Status.FINISHED.name().equals(flowStatus)) {
            FlowLogs.info(flowId, "flow {} run finished", flowId);
        }
    }
}
