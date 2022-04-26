package com.flow.eda.web.flow.status;

import com.flow.eda.web.flow.Flow;
import com.flow.eda.web.flow.FlowMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FlowStatusService {
    private static final String EXCHANGE = "flow.status";
    private static final String ROUTING_KEY = "flow.status.updated";
    /** 存储流程id对应的状态缓存对象 */
    private final Map<String, FlowStatus> statusMap = new HashMap<>();
    @Autowired private RabbitTemplate rabbitTemplate;
    @Autowired private FlowMapper flowMapper;

    public void updateStatus(Document payload) {
        String flowId = payload.getString("flowId");
        if (statusMap.containsKey(flowId)) {
            statusMap.get(flowId).update(payload);
        } else {
            statusMap.put(flowId, new FlowStatus(payload));
        }
        // 刷新看门狗
        FlowStatusWatchDog.refresh(flowId, this::updateStatus);
    }

    /** 将当前流程的状态更新到数据库 */
    private void updateStatus(String flowId) {
        Long id = Long.parseLong(flowId);
        if (!statusMap.containsKey(flowId)) {
            return;
        }
        Flow flow = flowMapper.findById(id);
        if (flow != null) {
            Flow.Status status = statusMap.get(flowId).getStatus();
            if (!flow.getStatus().equals(status)) {
                if (!Flow.Status.RUNNING.equals(status)) {
                    statusMap.remove(flowId);
                }
                // 流程状态有变化，发送到mq中，同时更新数据库
                this.sendFlowStatus(flowId, status.name());
                flowMapper.updateStatus(id, status.name());
            }
        }
    }

    /** 获取运行中的节点id */
    public List<String> getRunningNodes(String flowId) {
        if (statusMap.containsKey(flowId)) {
            return statusMap.get(flowId).getRunningNodes();
        }
        return null;
    }

    /** 向mq中发送流程的运行状态 */
    private void sendFlowStatus(String flowId, String status) {
        try {
            Document message = new Document("flowId", flowId).append("status", status);
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, message);
        } catch (Exception e) {
            log.error("send flow {} status to rabbitmq failed:{}", flowId, e.getMessage());
        }
    }
}
