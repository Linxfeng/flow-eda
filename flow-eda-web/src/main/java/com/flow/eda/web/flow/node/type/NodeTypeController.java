package com.flow.eda.web.flow.node.type;

import com.flow.eda.web.flow.node.data.NodeRequest;
import com.flow.eda.web.http.PageResult;
import com.flow.eda.web.log.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class NodeTypeController {
    @Autowired private NodeTypeService nodeTypeService;

    @OperationLog
    @GetMapping("/node/type")
    public PageResult<NodeType> getNodeTypes(NodeRequest request) {
        return PageResult.of(nodeTypeService.listByPage(request));
    }
}
