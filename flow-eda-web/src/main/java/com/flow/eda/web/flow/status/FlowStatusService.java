package com.flow.eda.web.flow.status;

import com.flow.eda.web.flow.Flow;
import com.flow.eda.web.flow.FlowMapper;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FlowStatusService {
    /** 存储流程id对应的状态缓存对象 */
    private final Map<String, FlowStatus> statusMap = new HashMap<>();

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
                // 最终状态时需要从map中移除当前流程
                if (!Flow.Status.RUNNING.equals(status)) {
                    statusMap.remove(flowId);
                }
                flowMapper.updateStatus(id, status.name());
            }
        }
    }
}
