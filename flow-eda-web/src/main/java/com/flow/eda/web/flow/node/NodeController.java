package com.flow.eda.web.flow.node;

import com.flow.eda.common.http.PageResult;
import com.flow.eda.common.http.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class NodeController {
    @Autowired private NodeService nodeService;

    @GetMapping("/node/type")
    public PageResult<NodeType> getNodeTypes(NodeRequest request) {
        return PageResult.of(nodeService.listByPage(request));
    }

    @PostMapping("/node/type")
    public Result<NodeType> addNodeType(@RequestBody NodeType nodeType) {
        return Result.of(nodeService.addNodeType(nodeType));
    }
}
