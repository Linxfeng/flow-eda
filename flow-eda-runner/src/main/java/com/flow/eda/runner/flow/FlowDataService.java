package com.flow.eda.runner.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FlowDataService {
    @Autowired private FlowDataMapper flowDataMapper;

    public void addFlowData(FlowData flowData) {
        flowData.setCreateDate(new Date());
        flowData.setUpdateDate(new Date());
        flowDataMapper.insert(flowData);
    }
}
