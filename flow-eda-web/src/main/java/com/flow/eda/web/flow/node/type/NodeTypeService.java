package com.flow.eda.web.flow.node.type;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @auther CodeGenerator
 * @create 2022-04-12 21:39:47
 * @describe 服务类
 */
public interface NodeTypeService extends IService<NodeType> {
    List<NodeType> findByTypeIds(List<Long> longs);
}
