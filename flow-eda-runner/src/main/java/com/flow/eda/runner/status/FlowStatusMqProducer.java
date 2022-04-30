package com.flow.eda.runner.status;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FlowStatusMqProducer {
    private static final String EXCHANGE = "flow.status";
    private static final String ROUTING_KEY = "flow.status.updated";
    @Autowired private RabbitTemplate rabbitTemplate;

    /** 向mq中发送流程的实时运行状态 */
    public void sendFlowStatus(String flowId, String status) {
        try {
            Document payload = new Document("flowId", flowId).append("status", status);
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, payload);
        } catch (Exception e) {
            log.error("send flow status to rabbitmq failed:{}", e.getMessage());
        }
    }
}
