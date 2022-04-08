package com.flow.eda.web.flow.node.data;

import com.flow.eda.web.flow.node.type.NodeType;
import com.flow.eda.web.flow.node.type.NodeTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.flow.eda.common.utils.CollectionUtil.*;

@Service
public class NodeDataService {
    @Autowired private NodeDataMapper nodeDataMapper;
    @Autowired private NodeTypeMapper nodeTypeMapper;

    public List<NodeData> getNodeData(Long flowId) {
        List<NodeData> list = nodeDataMapper.findByFlowId(flowId);
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        // 封装节点类型信息
        List<Long> typeIds = filterMap(list, Objects::nonNull, NodeData::getTypeId);
        if (isNotEmpty(typeIds)) {
            List<NodeType> nodeTypes = nodeTypeMapper.findByIds(typeIds);
            fillNodeType(list, nodeTypes);
        }
        return list;
    }

    public void updateNodeData(List<NodeData> data) {
        if (isNotEmpty(data)) {
            nodeDataMapper.insert(data);
        }
    }

    private void fillNodeType(List<NodeData> list, List<NodeType> types) {
        if (isNotEmpty(types)) {
            for (NodeType t : types) {
                list.stream()
                        .filter(n -> t.getId().equals(n.getTypeId()))
                        .findFirst()
                        .ifPresent(n -> n.setNodeType(t));
            }
        }
    }
}
