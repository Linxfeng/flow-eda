package com.flow.eda.web.flow.node.type.param;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NodeTypeParamMapper {

    @Select(
            "<script>SELECT * FROM eda_flow_node_type_param WHERE type_id IN "
                    + "<foreach collection='ids' item='id' open='(' separator=',' close=')'>"
                    + "#{id}</foreach></script>")
    List<NodeTypeParam> findByTypeIds(List<Long> ids);
}
