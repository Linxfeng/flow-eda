package com.flow.eda.logger.listener;

import com.flow.eda.logger.writer.LogFileWriter;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/** 接收业务操作日志 */
@Component
public class FlowOperationLogListener {
    @Autowired private LogFileWriter logFileWriter;

    @RabbitListener(
            bindings =
                    @QueueBinding(
                            value = @Queue(value = "flow_operation_log"),
                            exchange = @Exchange("flow.log"),
                            key = "flow.operation.log"))
    public void onRabbitMessage(@Payload String message) {
        logFileWriter.writeOperationLogs(message);
    }
}
