package com.flow.eda.web.flow.node.data;

import com.flow.eda.common.exception.InvalidStateException;
import com.flow.eda.common.exception.MissingPropertyException;
import com.flow.eda.common.http.Result;
import com.flow.eda.web.log.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.flow.eda.common.utils.CollectionUtil.isEmpty;

@RestController
@RequestMapping("/api/v1")
public class NodeDataController {
    @Autowired private NodeDataService nodeDataService;

    @OperationLog
    @GetMapping("/node/data")
    public Result<List<NodeData>> getNodeData(@RequestParam("flowId") String flowId) {
        return Result.of(nodeDataService.getNodeData(flowId));
    }

    @OperationLog
    @PostMapping("/node/data")
    public Result<String> setNodeData(@RequestBody List<NodeData> data) {
        this.check(data);
        nodeDataService.updateNodeData(data);
        return Result.ok();
    }

    @OperationLog
    @PostMapping("/node/data/version")
    public Result<String> saveVersion(
            @RequestParam String version, @RequestBody List<NodeData> data) {
        return Result.ok();
    }

    @OperationLog
    @PostMapping("/node/data/run")
    public Result<String> runNodeData(@RequestParam("flowId") String flowId) {
        nodeDataService.runNodeData(flowId);
        return Result.ok();
    }

    @OperationLog
    @PostMapping("/node/data/stop")
    public Result<String> stopNodeData(@RequestParam("flowId") String flowId) {
        nodeDataService.stopNodeData(flowId);
        return Result.ok();
    }

    /** 校验节点数据的正确性 */
    private void check(List<NodeData> data) {
        if (isEmpty(data)) {
            throw new InvalidStateException("The flow node data is empty");
        }
        // 校验必填参数
        for (NodeData node : data) {
            if (node.getId() == null) {
                throw new MissingPropertyException("id");
            }
            if (node.getFlowId() == null) {
                throw new MissingPropertyException("flowId");
            }
        }
    }
}
