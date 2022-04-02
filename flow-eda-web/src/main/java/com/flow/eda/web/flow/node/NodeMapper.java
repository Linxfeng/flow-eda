package com.flow.eda.web.flow.node;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NodeMapper {

    @Insert(
            "INSERT INTO eda_flow_node_type (type,type_name,svg,background,description) VALUES(#{type},#{typeName},#{svg},#{background},#{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(NodeType nodeType);

    @Select(
            "<script>SELECT * FROM eda_flow_node_type WHERE 1=1 "
                    + "<if test='name!=null'> AND type_name LIKE '%${name}%'</if></script>")
    List<NodeType> findByName(String name);
}
