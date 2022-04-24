package com.flow.eda.web.flow.status;

import com.flow.eda.web.flow.Flow;
import lombok.Getter;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** 流程状态缓存对象 */
@Getter
public class FlowStatus {
    private final String flowId;
    /** 存储节点id对应的信息 */
    private final Map<String, Document> nodeMap = new HashMap<>();

    public FlowStatus(Document payload) {
        this.flowId = payload.getString("flowId");
        this.update(payload);
    }

    /** 更新节点状态信息 */
    public void update(Document payload) {
        String nodeId = payload.getString("nodeId");
        if (nodeMap.containsKey(nodeId)) {
            // 对比消息的时间戳，兼容消息乱序问题，忽略过时消息
            Long timestamp = nodeMap.get(nodeId).getLong("timestamp");
            if (payload.getLong("timestamp") > timestamp) {
                nodeMap.put(nodeId, payload);
            }
        } else {
            nodeMap.put(nodeId, payload);
        }
    }

    /** 返回流程当前的状态 */
    public Flow.Status getStatus() {
        List<String> status =
                nodeMap.values().stream()
                        .map(p -> p.getString("status"))
                        .collect(Collectors.toList());
        if (status.contains(Flow.Status.FAILED.name())) {
            return Flow.Status.FAILED;
        }
        if (status.contains(Flow.Status.RUNNING.name())) {
            return Flow.Status.RUNNING;
        }
        return Flow.Status.FINISHED;
    }
}
