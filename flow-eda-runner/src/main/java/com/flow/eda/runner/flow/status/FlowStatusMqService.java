package com.flow.eda.runner.flow.status;

import com.flow.eda.runner.flow.node.Node;
import com.flow.eda.runner.flow.runtime.FlowThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FlowStatusMqService {
    private static final String EXCHANGE = "flow.node.status";
    private static final String ROUTING_KEY = "flow.node.status.updated";
    @Autowired private RabbitTemplate rabbitTemplate;
    @Autowired private FlowStatusWebSocket flowStatusWebSocket;

    /** 向mq中发送节点的运行状态 */
    public void sendNodeStatus(String flowId, String nodeId, String status) {
        try {
            Document message =
                    new Document("flowId", flowId)
                            .append("nodeId", nodeId)
                            .append("status", status);
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, message);
        } catch (Exception e) {
            log.error("send node status to rabbitmq failed:{}", e.getMessage());
        }
    }

    /** 接收mq中流程状态变化的消息 */
    @RabbitListener(
            bindings =
                    @QueueBinding(
                            value = @Queue(value = "flow_status_updated"),
                            exchange = @Exchange("flow.status"),
                            key = "flow.status.updated"))
    public void onRabbitMessage(@Payload Document payload) {
        // 推送流程状态信息
        String flowId = payload.getString("flowId");
        String status = payload.getString("status");
        flowStatusWebSocket.sendMessage(flowId, payload);
        // 流程运行结束，需要清理缓存数据
        if (!Node.Status.RUNNING.name().equals(status)) {
            FlowThreadPool.shutdownThreadPool(Long.parseLong(flowId));
            FlowThreadPool.shutdownSchedulerPool(Long.parseLong(flowId));
        }
    }
}
