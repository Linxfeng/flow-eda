package com.flow.eda.runner.flow.data;

import com.flow.eda.runner.flow.runtime.FlowDataRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlowDataService {
    @Autowired private FlowDataRuntime flowDataRuntime;

    public void saveFlowData(List<FlowData> data) {
        flowDataRuntime.putData(data);
    }

    public void runFlowData(Long id) {
        flowDataRuntime.runStartFlowData(id);
    }
}
