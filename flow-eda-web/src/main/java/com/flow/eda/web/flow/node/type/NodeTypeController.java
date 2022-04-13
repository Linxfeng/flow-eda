package com.flow.eda.web.flow.node.type;

import com.flow.eda.common.http.Result;
import com.flow.eda.web.flow.http.PageResult;
import com.flow.eda.web.flow.node.data.NodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class NodeTypeController {
    @Autowired private NodeTypeService nodeTypeService;

    @GetMapping("/node/type")
    public PageResult<NodeType> getNodeTypes(NodeRequest request) {
        return PageResult.of(nodeTypeService.listByPage(request));
    }

    @PostMapping("/node/type")
    public Result<NodeType> addNodeType(@RequestBody NodeType nodeType) {
        return Result.of(nodeTypeService.addNodeType(nodeType));
    }
}
