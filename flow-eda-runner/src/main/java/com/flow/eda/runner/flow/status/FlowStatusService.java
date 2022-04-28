package com.flow.eda.runner.flow.status;

import com.flow.eda.common.exception.InternalException;
import com.flow.eda.runner.flow.node.Node;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/** 流程状态服务，主要负责实时计算流程运行状态并监控其状态变更 */
@Service
public class FlowStatusService {
    /** 存放流程id对应的节点状态，仅存储运行中的节点 */
    private final Map<String, Map<String, String>> map = new HashMap<>();

    public void startRun(String flowId) {
        this.map.put(flowId, new HashMap<>());
    }

    /** 实时计算流程状态 */
    public String getFlowStatus(String flowId, Document message) {
        if (!map.containsKey(flowId)) {
            throw new InternalException(
                    "get flow status failed: can not find flowId in status map");
        }
        String nodeId = message.getString("nodeId");
        String status = message.getString("status");
        if (Node.Status.FINISHED.name().equals(status)) {
            map.get(flowId).remove(nodeId);
            if (map.get(flowId).isEmpty()) {
                return status;
            }
        } else if (Node.Status.RUNNING.name().equals(status)) {
            map.get(flowId).put(nodeId, status);
        } else if (Node.Status.FAILED.name().equals(status)) {
            map.get(flowId).remove(nodeId);
            if (map.get(flowId).isEmpty()) {
                return status;
            }
        }
        return Node.Status.RUNNING.name();
    }
}
