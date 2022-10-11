package com.flow.eda.runner.status;

import com.flow.eda.common.model.FlowData;
import com.flow.eda.common.utils.CollectionUtil;
import com.flow.eda.runner.node.Node;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static com.flow.eda.common.utils.CollectionUtil.*;

/** 流程状态服务，主要负责实时计算流程运行状态并监控其状态变更 */
@Service
public class FlowStatusService {
    /** 所有需要执行的节点 */
    private final Map<String, Set<String>> nodeMap = new ConcurrentHashMap<>();
    /** 正在运行的节点 */
    private final Map<String, Set<String>> runMap = new ConcurrentHashMap<>();
    /** 流程运行结束后会发起通知 */
    private final Map<String, Consumer<String>> noticeMap = new ConcurrentHashMap<>();

    @Autowired private FlowStatusClient flowStatusClient;

    public void startRun(
            String flowId, List<FlowData> data, List<FlowData> starts, List<FlowData> timer) {
        this.nodeMap.put(flowId, new HashSet<>());
        this.runMap.put(flowId, new HashSet<>());
        if (isNotEmpty(starts)) {
            this.parseAllNodes(flowId, data, starts);
        }
        if (isNotEmpty(timer)) {
            this.parseAllNodes(flowId, data, timer);
        }
    }

    /** 实时计算流程状态 */
    public String getFlowStatus(String flowId, Document message) {
        String nodeId = message.getString("nodeId");
        if (!nodeMap.containsKey(flowId) || nodeId == null) {
            return flowStatusClient.getFlowStatus(flowId).getResult();
        }
        String status = message.getString("status");
        if (Node.Status.FAILED.name().equals(status)) {
            nodeMap.get(flowId).remove(nodeId);
            runMap.get(flowId).remove(nodeId);
            return status;
        } else if (Node.Status.FINISHED.name().equals(status)) {
            nodeMap.get(flowId).remove(nodeId);
            runMap.get(flowId).remove(nodeId);
            if (nodeMap.get(flowId).isEmpty()) {
                return status;
            }
        } else {
            runMap.get(flowId).add(nodeId);
        }
        return Node.Status.RUNNING.name();
    }

    /** 判断当前流程状态是否已完成 */
    public boolean isFinished(String flowId) {
        if (!nodeMap.containsKey(flowId)) {
            String status = flowStatusClient.getFlowStatus(flowId).getResult();
            return Node.Status.FINISHED.name().equals(status);
        }
        return nodeMap.get(flowId).isEmpty();
    }

    /** 获取运行中的节点id */
    public List<String> getRunningNodes(String flowId) {
        Set<String> nodes = this.runMap.get(flowId);
        if (CollectionUtil.isNotEmpty(nodes)) {
            return new ArrayList<>(nodes);
        }
        return null;
    }

    /** 获取流程节点数据 */
    public List<FlowData> getFlowData(String flowId) {
        return flowInfoService.getFlowData(flowId);
    }

    /** 清理缓存数据 */
    public void clear(String flowId) {
        this.nodeMap.remove(flowId);
        this.runMap.remove(flowId);

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

    /** 解析出所有要执行的节点 */
    private void parseAllNodes(String flowId, List<FlowData> data, List<FlowData> starts) {
        forEach(
                starts,
                n -> {
                    this.nodeMap.get(flowId).add(n.getId());
                    parseNextNode(data, n, this.nodeMap.get(flowId));
                });
    }

    /** 解析出当前节点之后的所有节点 */
    private void parseNextNode(List<FlowData> data, FlowData currentNode, Set<String> nodeSet) {
        List<String> ids =
                filterMap(data, n -> currentNode.getId().equals(n.getFrom()), FlowData::getTo);
        if (isNotEmpty(ids)) {
            forEach(
                    filter(data, n -> ids.contains(n.getId())),
                    n -> {
                        nodeSet.add(n.getId());
                        parseNextNode(data, n, nodeSet);
                    });
        }
    }

    /** 移除当前节点之后的所有节点(包含当前节点) */
    public void removeNextNode(List<FlowData> data, FlowData currentNode) {
        Set<String> nodeSet = new HashSet<>();
        nodeSet.add(currentNode.getId());
        this.parseNextNode(data, currentNode, nodeSet);
        this.nodeMap.get(currentNode.getFlowId()).removeAll(nodeSet);
    }
}
