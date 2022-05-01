package com.flow.eda.logger.listener;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/** 接收业务操作日志 */
@Component
public class FlowOperationLogListener {

    @RabbitListener(
            bindings =
                    @QueueBinding(
                            value = @Queue(value = "flow_operation_log"),
                            exchange = @Exchange("flow.log"),
                            key = "flow.operation.log"))
    public void onRabbitMessage(@Payload String message) {
        System.out.println(message);
    }
}
