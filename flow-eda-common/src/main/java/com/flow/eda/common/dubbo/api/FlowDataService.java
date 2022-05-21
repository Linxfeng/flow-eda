package com.flow.eda.common.dubbo.api;

import com.flow.eda.common.dubbo.model.FlowData;

import java.util.List;

/** 流数据接口 */
public interface FlowDataService {
    /**
     * 运行当前流程
     *
     * @param data 流数据
     */
    void runFlowData(List<FlowData> data);

    /**
     * 停止当前流程
     *
     * @param flowId 流程id
     */
    void stopFlowData(String flowId);

    /**
     * 清理当前流程缓存数据（流程运行结束后调用）
     *
     * @param flowId 流程id
     */
    void clearFlowData(String flowId);
}
