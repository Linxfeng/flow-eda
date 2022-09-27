package com.flow.eda.web.flow.status;

import com.flow.eda.common.dubbo.api.FlowInfoService;
import com.flow.eda.common.dubbo.model.FlowData;
import com.flow.eda.web.flow.FlowService;
import com.flow.eda.web.flow.node.data.NodeDataService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DubboService(interfaceClass = FlowInfoService.class)
public class FlowInfoServiceImpl implements FlowInfoService {
    @Autowired private FlowService flowService;
    @Autowired private NodeDataService nodeDataService;

    @Override
    public String getFlowStatus(String flowId) {
        return flowService.findById(flowId).getStatus().name();
    }

    @Override
    public List<FlowData> getFlowData(String flowId) {
        return nodeDataService.queryNodeData(flowId);
    }
}
