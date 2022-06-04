package com.flow.eda.web.flow.node.type;

import com.flow.eda.web.flow.node.type.param.NodeTypeParam;
import com.flow.eda.web.flow.node.type.param.NodeTypeParamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.flow.eda.common.utils.CollectionUtil.*;

@Service
public class NodeTypeService {
    /** 定义节点类型的menu，并规定展示顺序 */
    private static final List<String> TYPE_MENU = Arrays.asList("基础", "解析", "网络", "数据库");
    @Autowired private NodeTypeMapper nodeTypeMapper;
    @Autowired private NodeTypeParamMapper nodeTypeParamMapper;

    public Map<String, List<NodeType>> getNodeTypes(String name) {
        Map<String, List<NodeType>> map = new LinkedHashMap<>();
        List<NodeType> list = nodeTypeMapper.findByName(name);
        mergeNodeTypeParam(list);
        // 分组，排序
        TYPE_MENU.forEach(m -> map.put(m, filter(list, t -> m.equals(t.getMenu()))));
        return map;
    }

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
