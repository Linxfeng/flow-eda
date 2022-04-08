package com.flow.eda.web.flow.node.type;

import com.flow.eda.web.flow.node.data.NodeRequest;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeTypeService {
    @Autowired private NodeTypeMapper nodeTypeMapper;

    public NodeType addNodeType(NodeType nodeType) {
        nodeTypeMapper.insert(nodeType);
        return nodeType;
    }

    public List<NodeType> listByPage(NodeRequest request) {
        PageHelper.startPage(request.getPage(), request.getLimit());
        return nodeTypeMapper.findByName(request.getName());
    }
}
