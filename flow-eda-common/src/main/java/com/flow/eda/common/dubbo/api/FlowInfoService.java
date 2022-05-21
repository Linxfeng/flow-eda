package com.flow.eda.common.dubbo.api;

/** 获取流程信息接口 */
public interface FlowInfoService {
    /**
     * 获取流程状态
     *
     * @param flowId 流程id
     * @return 返回流程当前状态
     */
    String getFlowStatus(String flowId);
}
