package com.flow.eda.runner.status;

import com.flow.eda.common.model.FlowData;
import com.flow.eda.runner.node.Node;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static com.flow.eda.common.utils.CollectionUtil.forEach;
import static com.flow.eda.common.utils.CollectionUtil.isNotEmpty;

/** 流程状态服务，主要负责实时计算流程运行状态并监控其状态变更 */
@Service
public class FlowStatusService {
    /** 正在运行的节点 */
    private final Map<String, Set<String>> runningMap = new ConcurrentHashMap<>();
    /** 流程运行结束后会发起通知 */
    private final Map<String, Consumer<String>> noticeMap = new ConcurrentHashMap<>();

    @Autowired private FlowStatusClient flowStatusClient;

    public void startRun(String flowId, List<FlowData> starts, List<FlowData> timer) {
        this.runningMap.put(flowId, new HashSet<>());
        forEach(starts, node -> this.runningMap.get(flowId).add(node.getId()));
        forEach(timer, node -> this.runningMap.get(flowId).add(node.getId()));
    }

    /** 实时计算流程状态 */
    public String getFlowStatus(String flowId, Document message) {
        String nodeId = message.getString("nodeId");
        if (nodeId == null) {
            return flowStatusClient.getFlowStatus(flowId).getResult();
        }
        String status = message.getString("status");
        if (Node.Status.FAILED.name().equals(status)) {
            runningMap.get(flowId).remove(nodeId);
            return status;
        } else if (Node.Status.FINISHED.name().equals(status)) {
            runningMap.get(flowId).remove(nodeId);
            if (runningMap.get(flowId).isEmpty()) {
                return status;
            }
        } else {
            runningMap.get(flowId).add(nodeId);
        }
        return Node.Status.RUNNING.name();
    }

    /** 判断当前流程状态是否已完成 */
    public boolean isFinished(String flowId) {
        if (!runningMap.containsKey(flowId)) {
            String status = flowStatusClient.getFlowStatus(flowId).getResult();
            return Node.Status.FINISHED.name().equals(status);
        }
        return runningMap.get(flowId).isEmpty();
    }

    /** 获取运行中的节点id */
    public List<String> getRunningNodes(String flowId) {
        Set<String> nodes = runningMap.get(flowId);
        if (isNotEmpty(nodes)) {
            return new ArrayList<>(nodes);
        }
        return null;
    }

    /** 添加运行中的节点 */
    public void addRunningNode(String flowId, String nodeId) {
        runningMap.computeIfAbsent(flowId, k -> new HashSet<>());
        runningMap.get(flowId).add(nodeId);
    }

    /** 移除运行中的节点 */
    public void removeRunningNode(String flowId, String nodeId) {
        Set<String> nodes = runningMap.get(flowId);
        if (isNotEmpty(nodes)) {
            nodes.remove(nodeId);
        }
    }

    /** 获取流程节点数据 */
    public List<FlowData> getFlowData(String flowId) {
        return flowStatusClient.getFlowData(flowId).getResult();
    }

    /** 清理缓存数据 */
    public void clear(String flowId) {
        this.runningMap.remove(flowId);

        // 流程运行结束后，发起通知
        if (noticeMap.containsKey(flowId)) {
            noticeMap.get(flowId).accept(flowId);
            noticeMap.remove(flowId);
        }
    }

    /** 注册通知服务，流程运行结束后会发起通知 */
    public void registerNotice(String flowId, Consumer<String> consumer) {
        noticeMap.put(flowId, consumer);
    }
}
