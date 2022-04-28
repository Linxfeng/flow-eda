package com.flow.eda.web.flow.status;

import org.bson.Document;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class FlowStatusListener {
    @Autowired private FlowStatusService flowStatusService;

    @RabbitListener(
            bindings =
                    @QueueBinding(
                            value = @Queue(value = "flow_status_updated"),
                            exchange = @Exchange("flow.status"),
                            key = "flow.status.updated"))
    public void onRabbitMessage(@Payload Document payload) {
        flowStatusService.update(payload);
    }
}
