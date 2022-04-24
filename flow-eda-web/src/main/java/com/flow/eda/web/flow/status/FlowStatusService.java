package com.flow.eda.web.flow.status;

import com.flow.eda.common.dubbo.api.FlowDataService;
import com.flow.eda.web.flow.Flow;
import com.flow.eda.web.flow.FlowMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FlowStatusService {
    /** 存储流程id对应的状态缓存对象 */
    private final Map<String, FlowStatus> statusMap = new HashMap<>();

    @DubboReference private FlowDataService flowDataService;
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
                // 流程运行结束后需要清理运行数据
                if (!Flow.Status.RUNNING.equals(status)) {
                    statusMap.remove(flowId);
                    flowDataService.stopFlowData(Long.parseLong(flowId));
                }
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
}
