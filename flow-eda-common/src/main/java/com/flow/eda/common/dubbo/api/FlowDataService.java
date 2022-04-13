package com.flow.eda.common.dubbo.api;

import com.flow.eda.common.dubbo.model.FlowData;

import java.util.List;

/***
 * 流数据接口
 */
public interface FlowDataService {
    /**
     * 运行一次当前流程
     *
     * @param data 流数据
     */
    void runFlowData(List<FlowData> data);
}
