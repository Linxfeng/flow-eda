package com.flow.eda.web.flow.status;

import org.bson.Document;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FlowStatusListener {
    @Autowired private FlowStatusService flowStatusService;

    @RabbitListener(
            bindings =
                    @QueueBinding(
                            value = @Queue(value = "flow_node_status_updated"),
                            exchange = @Exchange("flow.node.status"),
                            key = "flow.node.status.updated"))
    public void onRabbitMessage(@Payload Document payload, @Headers Map<String, Object> headers) {
        payload.append("timestamp", headers.get("timestamp"));
        flowStatusService.updateStatus(payload);
    }
}
