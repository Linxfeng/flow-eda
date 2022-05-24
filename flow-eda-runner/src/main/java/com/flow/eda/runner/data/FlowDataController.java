package com.flow.eda.runner.data;

import com.flow.eda.common.model.FlowData;
import com.flow.eda.runner.runtime.FlowDataRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 流程数据接口 */
@RestController
@RequestMapping("/api/v1/feign")
public class FlowDataController {
    @Autowired private FlowDataRuntime flowDataRuntime;

    /**
     * 运行当前流程
     *
     * @param data 流数据
     */
    @PostMapping("/flow/run")
    public void runFlowData(@RequestBody List<FlowData> data) {
        flowDataRuntime.runFlowData(data);
    }

    /**
     * 停止当前流程
     *
     * @param flowId 流程id
     */
    @PostMapping("/flow/stop")
    public void stopFlowData(@RequestParam String flowId) {
        flowDataRuntime.stopFlowData(flowId);
    }

    /**
     * 清理当前流程缓存数据（流程运行结束后调用）
     *
     * @param flowId 流程id
     */
    @PostMapping("/flow/clear")
    public void clearFlowData(@RequestParam String flowId) {
        flowDataRuntime.clearFlowData(flowId);
    }
}
