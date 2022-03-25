package com.flow.eda.runner.flow;

import com.flow.eda.common.http.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class FlowDataController {
    @Autowired private FlowDataService flowDataService;

    @PostMapping("/flow/data")
    public Result<FlowData> addFlow(@RequestBody FlowData flowData) {
        flowDataService.addFlowData(flowData);
        log.info("Create flow data id:{} success！", flowData.getId());
        return Result.of(flowData);
    }
}
