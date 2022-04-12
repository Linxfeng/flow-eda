package com.flow.eda.web.flow.node.data;

import com.flow.eda.common.exception.InvalidStateException;
import com.flow.eda.common.utils.MergeBuilder;
import com.flow.eda.web.flow.node.type.NodeType;
import com.flow.eda.web.flow.node.type.NodeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.flow.eda.common.utils.CollectionUtil.isEmpty;

@Service
public class NodeDataService {
    @Autowired private NodeDataMapper nodeDataMapper;
    @Autowired private NodeTypeService nodeTypeService;

    public List<NodeData> getNodeData(Long flowId) {
        List<NodeData> list = nodeDataMapper.findByFlowId(flowId);
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        // 封装节点类型信息
        return MergeBuilder.source(list, NodeData::getTypeId)
                .target(nodeTypeService::findByIds, NodeType::getId)
                .mergeS(NodeData::setNodeType);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateNodeData(List<NodeData> data) {
        Long flowId = data.get(0).getFlowId();
        nodeDataMapper.deleteByFlowId(flowId);
        nodeDataMapper.insert(data);
    }

    public void runNodeData(Long flowId) {
        List<NodeData> list = nodeDataMapper.findByFlowId(flowId);
        if (isEmpty(list)) {
            throw new InvalidStateException("The flow data is empty, cannot deploy");
        }
        // 调用接口-todo
    }
}
