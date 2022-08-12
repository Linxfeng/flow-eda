package com.flow.eda.web.flow.node.data;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NodeDataMapper {

    @Insert(
            "<script>INSERT INTO eda_flow_node_data (id, node_name, flow_id, type_id, `top`, `left`, remark, "
                    + "params, payload, `from`, `to`, version ) VALUES "
                    + "<foreach collection='list' item='n' separator=','>("
                    + "#{n.id}, #{n.nodeName}, #{n.flowId}, #{n.typeId}, #{n.top}, #{n.left}, #{n.remark}, "
                    + "#{n.params}, #{n.payload}, #{n.from}, #{n.to}, #{n.version})</foreach></script>")
    void insert(List<NodeData> list);

    @Select("SELECT * FROM eda_flow_node_data WHERE flow_id=#{flowId} AND version IS NULL")
    List<NodeData> findByFlowId(String flowId);

    @Delete("DELETE FROM eda_flow_node_data WHERE flow_id=#{flowId}")
    void deleteByFlowId(String flowId);

    @Delete("DELETE FROM eda_flow_node_data WHERE flow_id=#{flowId} AND version IS NULL")
    void deleteDataByFlowId(String flowId);
}
