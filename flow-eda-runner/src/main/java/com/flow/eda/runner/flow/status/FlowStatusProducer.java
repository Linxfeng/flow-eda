package com.flow.eda.runner.flow.status;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** 向mq中推送节点运行状态信息，用于判断流程的最终运行状态 */
@Slf4j
@Component
public class FlowStatusProducer {
    private static final String EXCHANGE = "flow.node.status";
    private static final String ROUTING_KEY = "flow.status.updated";
    @Autowired private RabbitTemplate rabbitTemplate;

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
}
