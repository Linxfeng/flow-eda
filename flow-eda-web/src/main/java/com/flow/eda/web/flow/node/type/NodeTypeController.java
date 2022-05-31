package com.flow.eda.web.flow.node.type;

import com.flow.eda.common.http.Result;
import com.flow.eda.web.log.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class NodeTypeController {
    @Autowired private NodeTypeService nodeTypeService;

    @OperationLog
    @GetMapping("/node/type")
    public Result<Map<String, List<NodeType>>> getNodeTypes(
            @RequestParam(required = false) String name) {
        return Result.of(nodeTypeService.getNodeTypes(name));
    }
}
