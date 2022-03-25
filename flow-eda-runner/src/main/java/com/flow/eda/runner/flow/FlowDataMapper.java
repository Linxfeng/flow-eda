package com.flow.eda.runner.flow;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface FlowDataMapper {

    @Insert(
            "INSERT INTO eda_flow_data VALUES(#{id},#{script},#{executeCount},#{lastExecuteDate},#{createDate},#{updateDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(FlowData flowData);
}
