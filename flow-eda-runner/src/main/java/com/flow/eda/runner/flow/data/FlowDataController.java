package com.flow.eda.runner.flow.data;

import com.flow.eda.common.exception.InvalidStateException;
import com.flow.eda.common.http.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class FlowDataController {
    @Autowired private FlowDataService flowDataService;

    @PostMapping("/flow/data")
    public Result<String> saveFlowData(@RequestBody List<FlowData> data) {
        if (data.isEmpty()) {
            throw new InvalidStateException("The flow data cannot be empty");
        }
        flowDataService.saveFlowData(data);
        log.info("Save flow data id {} success", data.get(0).getFlowId());
        return Result.ok();
    }

    @PostMapping("/flow/data/run")
    public Result<String> runFlowData(@RequestParam("id") Long id) {
        flowDataService.runFlowData(id);
        log.info("Run once flow data id {} success", id);
        return Result.ok();
    }
}
