package com.flow.eda.web.flow.node;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeService {
    @Autowired private NodeMapper nodeMapper;

    public NodeType addNodeType(NodeType nodeType) {
        nodeMapper.insert(nodeType);
        return nodeType;
    }

    public List<NodeType> listByPage(NodeRequest request) {
        PageHelper.startPage(request.getPage(), request.getLimit());
        return nodeMapper.findByName(request.getName());
    }
}
