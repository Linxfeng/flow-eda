package com.flow.eda.common.dubbo.api;

import com.flow.eda.common.dubbo.model.FlowData;

import java.util.List;

/** 获取流程信息接口 */
public interface FlowInfoService {
    /**
     * 获取流程状态
     *
     * @param flowId 流程id
     * @return 返回流程当前状态
     */
    String getFlowStatus(String flowId);

    /**
     * 获取流程节点数据
     *
     * @param flowId 流程id
     * @return 返回流程节点数据
     */
    List<FlowData> getFlowData(String flowId);
}
