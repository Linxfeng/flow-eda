package com.flow.eda.runner.status;

import com.flow.eda.common.dubbo.api.FlowInfoService;
import com.flow.eda.common.dubbo.model.FlowData;
import com.flow.eda.runner.node.Node;
import org.apache.dubbo.config.annotation.DubboReference;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.flow.eda.common.utils.CollectionUtil.*;

/** 流程状态服务，主要负责实时计算流程运行状态并监控其状态变更 */
@Service
public class FlowStatusService {
    /** 所有需要执行的节点 */
    private final Map<String, Set<String>> nodeMap = new HashMap<>();
    /** 正在运行的节点 */
    private final Map<String, Set<String>> runMap = new HashMap<>();
    /** 临时存储流数据 */
    private final Map<String, List<FlowData>> dataMap = new HashMap<>();

    @DubboReference private FlowInfoService flowInfoService;

    public void startRun(
            String flowId, List<FlowData> data, List<FlowData> starts, List<FlowData> timer) {
        this.dataMap.put(flowId, data);
        this.nodeMap.put(flowId, new HashSet<>());
        this.runMap.put(flowId, new HashSet<>());
        if (isNotEmpty(starts)) {
            this.parseAllNodes(flowId, starts);
        }
        if (isNotEmpty(timer)) {
            this.parseAllNodes(flowId, timer);
        }
        this.dataMap.remove(flowId);
    }

    /** 实时计算流程状态 */
    public String getFlowStatus(String flowId, Document message) {
        if (!nodeMap.containsKey(flowId)) {
            return flowInfoService.getFlowStatus(flowId);
        }
        String nodeId = message.getString("nodeId");
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

    /** 获取运行中的节点id */
    public List<String> getRunningNodes(String flowId) {
        Set<String> nodes = this.runMap.get(flowId);
        if (!nodes.isEmpty()) {
            return new ArrayList<>(nodes);
        }
        return null;
    }

    /** 清理缓存数据 */
    public void clear(String flowId) {
        this.nodeMap.remove(flowId);
        this.runMap.remove(flowId);
    }

    /** 解析出所有要执行的节点 */
    private void parseAllNodes(String flowId, List<FlowData> starts) {
        forEach(
                starts,
                n -> {
                    this.nodeMap.get(flowId).add(n.getId());
                    parseNextNode(flowId, n);
                });
    }

    private void parseNextNode(String flowId, FlowData currentNode) {
        List<FlowData> data = this.dataMap.get(flowId);
        List<String> ids =
                filterMap(data, n -> currentNode.getId().equals(n.getFrom()), FlowData::getTo);
        if (isNotEmpty(ids)) {
            forEach(
                    filter(data, n -> ids.contains(n.getId())),
                    n -> {
                        this.nodeMap.get(flowId).add(n.getId());
                        parseNextNode(flowId, n);
                    });
        }
    }
}
