package com.flow.eda.web.flow.node.data;

import com.flow.eda.common.http.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class NodeDataController {
    @Autowired private NodeDataService nodeDataService;

    @GetMapping("/node/data")
    public Result<List<NodeData>> getNodeData(@RequestParam("flowId") Long flowId) {
        return Result.of(nodeDataService.getNodeData(flowId));
    }

    @PostMapping("/node/data")
    public Result<String> setNodeData(@RequestBody List<NodeData> data) {
        nodeDataService.updateNodeData(data);
        return Result.ok();
    }
}
