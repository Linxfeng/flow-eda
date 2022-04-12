package com.flow.eda.web.flow.node.data;

import com.flow.eda.common.exception.InvalidStateException;
import com.flow.eda.common.exception.MissingPropertyException;
import com.flow.eda.common.http.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.flow.eda.common.utils.CollectionUtil.isEmpty;

@RestController
@RequestMapping("/api/v1")
public class NodeDataController {
    @Autowired private NodeDataServiceImpl nodeDataServiceImpl;

    @GetMapping("/node/data")
    public Result<List<NodeData>> getNodeData(@RequestParam("flowId") Long flowId) {
        return Result.of(nodeDataServiceImpl.getNodeData(flowId));
    }

    @PostMapping("/node/data")
    public Result<String> setNodeData(@RequestBody List<NodeData> data) {
        this.check(data);
        nodeDataServiceImpl.updateNodeData(data);
        return Result.ok();
    }

    /** 校验节点数据的正确性 */
    private void check(List<NodeData> data) {
        if (isEmpty(data)) {
            throw new InvalidStateException("The flow node data is empty");
        }
        for (NodeData node : data) {
            // 校验必填参数
            if (node.getId() == null) {
                throw new MissingPropertyException("id");
            }
            if (node.getFlowId() == null) {
                throw new MissingPropertyException("flowId");
            }
            if (node.getNodeName() != null && node.getTypeId() == null) {
                throw new MissingPropertyException("typeId");
            }
            // 待完善...
        }
    }
}
