package com.flow.eda.web.flow.node.type;

import com.flow.eda.web.flow.node.type.param.NodeTypeParam;
import com.flow.eda.web.flow.node.type.param.NodeTypeParamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.flow.eda.common.utils.CollectionUtil.*;

@Service
public class NodeTypeService {
    @Autowired private NodeTypeMapper nodeTypeMapper;
    @Autowired private NodeTypeParamMapper nodeTypeParamMapper;

    public List<NodeType> findByIds(List<Long> ids) {
        List<NodeType> list = nodeTypeMapper.findByIds(ids);
        return mergeNodeTypeParam(list);
    }

    /** 封装节点类型参数信息 */
    private List<NodeType> mergeNodeTypeParam(List<NodeType> list) {
        if (isEmpty(list)) {
            return list;
        }
        List<Long> ids = map(list, NodeType::getId);
        List<NodeTypeParam> params = nodeTypeParamMapper.findByTypeIds(ids);
        if (isNotEmpty(params)) {
            list.forEach(t -> t.setParams(filter(params, p -> t.getId().equals(p.getTypeId()))));
        }
        return list;
    }
}
