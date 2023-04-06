package com.flow.eda.web.flow.status;

import com.flow.eda.common.http.Result;
import com.flow.eda.common.model.FlowData;
import com.flow.eda.web.flow.FlowService;
import com.flow.eda.web.flow.node.data.NodeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 获取流程状态信息接口 */
@RestController
@RequestMapping("/api/v1/feign")
public class FlowStatusController {
    @Autowired private FlowService flowService;
    @Autowired private NodeDataService nodeDataService;

    /**
     * 获取流程状态
     *
     * @param flowId 流程id
     * @return 返回流程当前状态
     */
    @GetMapping("/flow/status")
    public Result<String> getFlowStatus(@RequestParam String flowId) {
        return Result.of(flowService.findById(flowId).getStatus().name());
    }

    /**
     * 获取流程状态
     *
     * @param flowId 流程id
     * @return 返回流程当前状态
     */
    @GetMapping("/flow/data")
    public Result<List<FlowData>> getFlowData(@RequestParam String flowId) {
        return Result.of(nodeDataService.queryNodeData(flowId));
    }
}
