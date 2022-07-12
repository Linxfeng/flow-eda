package com.flow.eda.runner.status;

import com.flow.eda.common.http.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** 获取流程状态信息接口 */
@FeignClient(name = "flowStatus", url = "localhost:8081")
public interface FlowStatusClient {
    /**
     * 获取流程状态
     *
     * @param flowId 流程id
     * @return 返回流程当前状态
     */
    @GetMapping("/api/v1/feign/flow/status")
    Result<String> getFlowStatus(@RequestParam String flowId);
}
