package com.flow.eda.web.flow.node.data;

import com.flow.eda.common.model.FlowData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/** 流程数据接口 */
@FeignClient(name = "flowData", url = "localhost:8088")
public interface FlowDataClient {
    /**
     * 运行当前流程
     *
     * @param data 流数据
     */
    @PostMapping("/api/v1/feign/flow/run")
    void runFlowData(@RequestBody List<FlowData> data);

    /**
     * 停止当前流程
     *
     * @param flowId 流程id
     */
    @PostMapping("/api/v1/feign/flow/stop")
    void stopFlowData(@RequestParam String flowId);

    /**
     * 清理当前流程缓存数据（流程运行结束后调用）
     *
     * @param flowId 流程id
     */
    @PostMapping("/api/v1/feign/flow/clear")
    void clearFlowData(@RequestParam String flowId);
}
