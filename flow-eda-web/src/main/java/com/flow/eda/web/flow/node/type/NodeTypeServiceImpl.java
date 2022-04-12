package com.flow.eda.web.flow.node.type;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flow.eda.web.flow.node.data.NodeRequest;
import com.flow.eda.web.flow.node.type.param.NodeTypeParam;
import com.flow.eda.web.flow.node.type.param.NodeTypeParamService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.flow.eda.common.utils.CollectionUtil.*;

@Service
public class NodeTypeServiceImpl extends ServiceImpl<NodeTypeMapper, NodeType>
        implements NodeTypeService {

    @Autowired private NodeTypeParamService nodeTypeParamService;

    public NodeType addNodeType(NodeType nodeType) {
        this.save(nodeType);
        return nodeType;
    }

    public List<NodeType> listByPage(NodeRequest request) {
        PageHelper.startPage(request.getPage(), request.getLimit());
        List<NodeType> list =
                this.list(
                        new LambdaQueryWrapper<NodeType>()
                                .like(NodeType::getTypeName, request.getName()));
        return mergeNodeTypeParam(list);
    }

    public List<NodeType> findByTypeIds(List<Long> ids) {
        List<NodeType> list = this.listByIds(ids);
        return mergeNodeTypeParam(list);
    }

    /** 封装节点类型参数信息 */
    private List<NodeType> mergeNodeTypeParam(List<NodeType> list) {
        if (isEmpty(list)) {
            return list;
        }
        List<Long> ids = map(list, NodeType::getId);
        List<NodeTypeParam> params = nodeTypeParamService.listByIds(ids);
        if (isNotEmpty(params)) {
            list.forEach(t -> t.setParams(filter(params, p -> t.getId().equals(p.getTypeId()))));
        }
        return list;
    }
}
