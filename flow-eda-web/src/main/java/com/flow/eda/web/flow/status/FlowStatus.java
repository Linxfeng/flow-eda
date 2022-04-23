package com.flow.eda.web.flow.status;

import lombok.Getter;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

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

    /** 判断当前流程是否已完成 */
    public boolean isFinished() {
        //
        return false;
    }
}
