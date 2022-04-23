package com.flow.eda.web.flow.status;

import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FlowStatusService {
    /** 存储流程id对应的状态缓存对象 */
    private final Map<String, FlowStatus> statusMap = new HashMap<>();

    public void updateStatus(Document payload) {
        String flowId = payload.getString("flowId");
        if (statusMap.containsKey(flowId)) {
            statusMap.get(flowId).update(payload);
        } else {
            statusMap.put(flowId, new FlowStatus(payload));
        }
        // 刷新看门狗
        FlowStatusWatchDog.refresh(flowId, this::calculateStatus);
    }

    /** 立即计算一次当前流程的状态 */
    private void calculateStatus(String flowId) {
        FlowStatus flowStatus = statusMap.get(flowId);
        if (flowStatus.isFinished()) {
            // 更新流程状态
        }
    }
}
