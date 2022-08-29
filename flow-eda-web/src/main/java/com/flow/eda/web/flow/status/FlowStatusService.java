package com.flow.eda.web.flow.status;

import com.flow.eda.common.utils.CollectionUtil;
import com.flow.eda.web.flow.Flow;
import com.flow.eda.web.flow.FlowMapper;
import com.flow.eda.web.flow.FlowRequest;
import com.flow.eda.web.flow.node.data.FlowDataClient;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FlowStatusService {
    private final Map<String, String> statusMap = new HashMap<>();
    @Autowired private FlowDataClient flowDataClient;
    @Autowired private FlowMapper flowMapper;

    /** 刷新缓存中的流程状态 */
    public void update(Document payload) {
        String flowId = payload.getString("flowId");
        String status = payload.getString("status");
        statusMap.put(flowId, status);
        // 刷新看门狗
        FlowStatusWatchDog.refresh(flowId, this::updateStatus);
    }

    /** 将当前流程的状态更新到数据库 */
    private void updateStatus(String flowId) {
        if (!statusMap.containsKey(flowId)) {
            return;
        }
        Flow flow = flowMapper.findById(flowId);
        if (flow != null) {
            String status = statusMap.get(flowId);
            // 将流程状态更新到数据库
            if (!flow.getStatus().name().equals(status)) {
                flowMapper.updateStatus(flowId, status);
            }
            // 如果流程已执行完毕，则需要清理缓存数据
            if (!Flow.Status.RUNNING.name().equals(status)) {
                statusMap.remove(flowId);
                flowDataClient.clearFlowData(flowId);
            }
        }
    }

    /** 测试账号的定时任务：每日0点测试账号数据重置之前(1分钟)，停止正在运行中的流程 */
    @Scheduled(cron = "0 1 0 * * ?")
    public void stopRunningFlow() {
        FlowRequest request = new FlowRequest();
        request.setStatus(Flow.Status.RUNNING);
        request.setUsername("test");
        List<Flow> flowList = flowMapper.findByRequest(request);
        CollectionUtil.forEach(flowList, flow -> flowDataClient.stopFlowData(flow.getId()));
    }
}
