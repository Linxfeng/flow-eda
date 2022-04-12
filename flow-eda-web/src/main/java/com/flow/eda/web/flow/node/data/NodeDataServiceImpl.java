package com.flow.eda.web.flow.node.data;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class NodeDataServiceImpl extends ServiceImpl<NodeDataMapper, NodeData>
        implements NodeDataService {
    @Autowired private NodeTypeService nodeTypeService;

    @Override
    public List<NodeData> getNodeData(Long flowId) {
        List<NodeData> list =
                this.list(new LambdaQueryWrapper<NodeData>().eq(NodeData::getFlowId, flowId));
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        // 封装节点类型信息
        return MergeBuilder.source(list, NodeData::getTypeId)
                .target(nodeTypeService::findByTypeIds, NodeType::getId)
                .mergeS(NodeData::setNodeType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateNodeData(List<NodeData> data) {
        Long flowId = data.get(0).getFlowId();
        this.remove(new LambdaQueryWrapper<NodeData>().eq(NodeData::getFlowId, flowId));
        this.saveBatch(data);
    }

    @Override
    public void runNodeData(Long flowId) {
        List<NodeData> list =
                this.list(new LambdaQueryWrapper<NodeData>().eq(NodeData::getFlowId, flowId));
        if (isEmpty(list)) {
            throw new InvalidStateException("The flow data is empty, cannot deploy");
        }
        // 调用接口-todo
    }
}
