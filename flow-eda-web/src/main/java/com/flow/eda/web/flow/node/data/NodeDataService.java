package com.flow.eda.web.flow.node.data;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @auther CodeGenerator
 * @create 2022-04-12 21:40:58
 * @describe 服务类
 */
public interface NodeDataService extends IService<NodeData> {
    void runNodeData(Long flowId);

    List<NodeData> getNodeData(Long flowId);

    void updateNodeData(List<NodeData> data);
}
