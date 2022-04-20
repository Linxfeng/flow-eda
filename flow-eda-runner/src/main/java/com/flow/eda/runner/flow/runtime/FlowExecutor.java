package com.flow.eda.runner.flow.runtime;

import com.flow.eda.common.dubbo.model.FlowData;
import com.flow.eda.common.exception.InternalException;
import com.flow.eda.runner.flow.data.FlowWebSocket;
import com.flow.eda.runner.flow.node.Node;
import com.flow.eda.runner.flow.node.NodeTypeEnum;
import org.bson.Document;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.flow.eda.common.utils.CollectionUtil.*;

/** 单条流程的执行者 */
public class FlowExecutor {
    /** 用于并发执行流程中的多个节点的线程池 */
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    /** 存储当前流程的完整流节点数据 */
    private final List<FlowData> flowData;
    /** 用于推送消息的ws服务 */
    private final FlowWebSocket flowWebSocket;
    /** 当前流程的id */
    private final Long flowId;

    public FlowExecutor(List<FlowData> flowData, FlowWebSocket ws) {
        this.flowData = flowData;
        this.flowWebSocket = ws;
        this.flowId =
                Objects.requireNonNull(findFirst(flowData, n -> n.getFlowId() != null)).getFlowId();
    }

    /** 开始执行流程 */
    public void start(FlowData startNode) {
        this.run(startNode);
    }

    /** 执行当前节点 */
    private void run(FlowData currentNode) {
        Node nodeInstance = getInstance(currentNode);
        sendNodeStatus(currentNode.getId(), new Document("status", nodeInstance.status().name()));
        try {
            nodeInstance.run((p) -> runNext(currentNode, p));
        } catch (Exception e) {
            Document msg =
                    new Document("status", Node.Status.FAILED.name())
                            .append("error", e.getMessage());
            sendNodeStatus(currentNode.getId(), msg);
        }
        if (!NodeTypeEnum.OUTPUT.getType().equals(currentNode.getType())
                && Node.Status.FINISHED.equals(nodeInstance.status())) {
            sendNodeStatus(
                    currentNode.getId(), new Document("status", nodeInstance.status().name()));
        }
    }

    /** 节点数据执行后回调，继续执行下一节点 */
    private void runNext(FlowData currentNode, Document p) {
        List<FlowData> nextNodes = getNextNode(currentNode);
        // 多个下游节点，需要并行执行
        forEach(nextNodes, n -> threadPool.execute(() -> this.run(setInput(n, p))));
        // 输出节点推送消息
        if (NodeTypeEnum.OUTPUT.getType().equals(currentNode.getType())) {
            Document output = new Document();
            output.putAll(p);
            output.remove("input");
            output.remove("payload");
            Document msg =
                    new Document("status", Node.Status.FINISHED.name()).append("output", output);
            sendNodeStatus(currentNode.getId(), msg);
        }
    }

    private List<FlowData> getNextNode(FlowData currentNode) {
        List<String> ids =
                filterMap(flowData, n -> currentNode.getId().equals(n.getFrom()), FlowData::getTo);
        if (isNotEmpty(ids)) {
            return filter(flowData, n -> ids.contains(n.getId()));
        }
        return null;
    }

    /** 获取当前节点的实例 */
    private Node getInstance(FlowData currentNode) {
        try {
            // 获取节点的构造函数，默认每个节点都有含参构造，获取不到时抛出异常
            Class<? extends Node> clazz = NodeTypeEnum.getClazzByNode(currentNode);
            return clazz.getConstructor(Document.class).newInstance(currentNode.getParams());
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    /** 设置input，上游节点的输出参数需要传递至下一节点 */
    private FlowData setInput(FlowData currentNode, Document input) {
        if (input != null) {
            Document params = Optional.ofNullable(currentNode.getParams()).orElseGet(Document::new);
            params.putAll(input);
            currentNode.setParams(params);
        }
        return currentNode;
    }

    private void sendNodeStatus(String nodeId, Document msg) {
        msg.append("nodeId", nodeId);
        flowWebSocket.sendMessage(String.valueOf(flowId), msg.toJson());
    }
}
