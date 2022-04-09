package com.flow.eda.web.flow.node.type;

import com.flow.eda.web.flow.node.data.NodeRequest;
import com.flow.eda.web.flow.node.type.param.NodeTypeParam;
import com.flow.eda.web.flow.node.type.param.NodeTypeParamMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.flow.eda.common.utils.CollectionUtil.*;

@Service
public class NodeTypeService {
    @Autowired private NodeTypeMapper nodeTypeMapper;
    @Autowired private NodeTypeParamMapper nodeTypeParamMapper;

    public NodeType addNodeType(NodeType nodeType) {
        nodeTypeMapper.insert(nodeType);
        return nodeType;
    }

    public List<NodeType> listByPage(NodeRequest request) {
        PageHelper.startPage(request.getPage(), request.getLimit());
        List<NodeType> list = nodeTypeMapper.findByName(request.getName());
        if (isEmpty(list)) {
            return list;
        }
        // 封装节点类型参数信息
        List<Long> ids = map(list, NodeType::getId);
        List<NodeTypeParam> params = nodeTypeParamMapper.findByTypeIds(ids);
        if (isNotEmpty(params)) {
            list.forEach(t -> t.setParam(filter(params, p -> t.getId().equals(p.getTypeId()))));
        }
        return list;
    }
}
