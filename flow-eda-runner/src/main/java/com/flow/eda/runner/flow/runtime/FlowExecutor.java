package com.flow.eda.runner.flow.runtime;

import com.flow.eda.common.dubbo.model.FlowData;
import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.flow.node.Node;
import com.flow.eda.runner.flow.node.NodeTypeEnum;
import com.flow.eda.runner.flow.status.FlowWebSocket;
import org.bson.Document;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.flow.eda.common.utils.CollectionUtil.*;
import static com.flow.eda.runner.flow.runtime.FlowThreadPool.getThreadPool;

/** 单条流程的执行者 */
public class FlowExecutor {
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
        Thread.currentThread().setName("flowId:" + flowId);
        try {
            Node nodeInstance = getInstance(currentNode);
            sendNodeStatus(
                    currentNode.getId(), new Document("status", nodeInstance.status().name()));
            nodeInstance.run((p) -> runNext(currentNode, nodeInstance, p));
        } catch (Exception e) {
            String message;
            if (e.getMessage() != null) {
                message = FlowException.wrap(e, e.getMessage()).getMessage();
            } else {
                message = FlowException.wrap(e).getMessage();
            }
            Document status = new Document("status", Node.Status.FAILED.name());
            sendNodeStatus(currentNode.getId(), status.append("error", message));
        }
    }

    /** 节点数据执行后回调，继续执行下一节点 */
    private void runNext(FlowData currentNode, Node nodeInstance, Document p) {
        // 推送节点消息
        if (NodeTypeEnum.OUTPUT.getType().equals(currentNode.getType())) {
            sendOutput(currentNode, p);
        } else if (Node.Status.FINISHED.equals(nodeInstance.status())) {
            sendNodeStatus(
                    currentNode.getId(), new Document("status", Node.Status.FINISHED.name()));
        }
        // 多个下游节点，需要并行执行
        List<FlowData> nextNodes = getNextNode(currentNode);
        forEach(nextNodes, n -> getThreadPool(flowId).execute(() -> this.run(setInput(n, p))));
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
            // 初始化实例时会进行参数校验，校验不通过则会抛出异常
            return clazz.getConstructor(Document.class).newInstance(currentNode.getParams());
        } catch (Exception e) {
            throw FlowException.wrap(e);
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

    /** 输出节点发送节点状态和输出信息 */
    private void sendOutput(FlowData currentNode, Document p) {
        Document output = new Document();
        output.putAll(p);
        output.remove("input");
        output.remove("payload");
        Document msg = new Document("status", Node.Status.FINISHED.name()).append("output", output);
        sendNodeStatus(currentNode.getId(), msg);
    }

    private void sendNodeStatus(String nodeId, Document msg) {
        msg.append("nodeId", nodeId);
        flowWebSocket.sendMessage(String.valueOf(flowId), msg);
    }
}
