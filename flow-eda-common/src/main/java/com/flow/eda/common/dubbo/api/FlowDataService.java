package com.flow.eda.common.dubbo.api;

import com.flow.eda.common.dubbo.model.FlowData;

import java.util.List;

/***
 * 流数据接口
 */
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
    void stopFlowData(Long flowId);

    /**
     * 用于通知运行服务正在运行中的节点id
     *
     * @param flowId 流程id
     * @param nodeIds 运行中的节点id
     */
    void noticeRunningNodes(Long flowId, List<String> nodeIds);
}
