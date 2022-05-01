package com.flow.eda.logger.listener;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/** 接收流程运行日志 */
@Component
public class FlowRunningLogListener {
    private static final String SEPARATOR = "#flowId:";

    @RabbitListener(
            bindings =
                    @QueueBinding(
                            value = @Queue(value = "flow_running_log"),
                            exchange = @Exchange("flow.log"),
                            key = "flow.running.log"))
    public void onRabbitMessage(@Payload String message) {
        if (message.contains(SEPARATOR)) {
            String log = message.split(SEPARATOR)[0];
            String flowId = message.split(SEPARATOR)[1];
            System.out.println(flowId);
            System.out.println(log);
        }
    }
}
