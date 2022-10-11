package com.flow.eda.web.flow.status;

import com.flow.eda.common.dubbo.api.FlowInfoService;
import com.flow.eda.web.flow.FlowService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DubboService(interfaceClass = FlowInfoService.class)
public class FlowInfoServiceImpl implements FlowInfoService {
    @Autowired private FlowService flowService;

    @Override
    public String getFlowStatus(String flowId) {
        return flowService.findById(flowId).getStatus().name();
    }
}
