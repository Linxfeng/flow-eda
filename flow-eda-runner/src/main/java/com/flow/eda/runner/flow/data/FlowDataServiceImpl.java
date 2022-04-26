package com.flow.eda.runner.flow.data;

import com.flow.eda.common.dubbo.api.FlowDataService;
import com.flow.eda.common.dubbo.model.FlowData;
import com.flow.eda.runner.flow.runtime.FlowDataRuntime;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DubboService(interfaceClass = FlowDataService.class)
public class FlowDataServiceImpl implements FlowDataService {
    @Autowired private FlowDataRuntime flowDataRuntime;

    @Override
    public void runFlowData(List<FlowData> data) {
        flowDataRuntime.runFlowData(data);
    }

    @Override
    public void stopFlowData(String flowId) {
        flowDataRuntime.stopFlowData(flowId);
    }

    @Override
    public void noticeRunningNodes(String flowId, List<String> nodeIds) {
        flowDataRuntime.sendNodeInterruptStatus(flowId, nodeIds);
    }
}
