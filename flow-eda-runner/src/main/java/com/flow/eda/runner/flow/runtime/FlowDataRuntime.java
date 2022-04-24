package com.flow.eda.runner.flow.runtime;

import com.flow.eda.common.dubbo.model.FlowData;
import com.flow.eda.runner.flow.node.Node;
import com.flow.eda.runner.flow.node.NodeTypeEnum;
import com.flow.eda.runner.flow.status.FlowWebSocket;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

import static com.flow.eda.common.utils.CollectionUtil.*;
import static com.flow.eda.runner.flow.runtime.FlowThreadPool.getThreadPool;

@Service
public class FlowDataRuntime {
    @Autowired private FlowWebSocket ws;

    /** 应用启动时，需要加载出正在运行中的流数据，继续运行 */
    @PostConstruct
    public void loadingFlowData() {}

    /** 运行流程 */
    public void runFlowData(List<FlowData> data) {
        Long flowId =
                Objects.requireNonNull(findFirst(data, n -> n.getFlowId() != null)).getFlowId();
        // 将流数据分为开始节点和定时器节点进行分别执行
        List<FlowData> starts = filter(data, this::isStartNode);
        forEach(
                starts,
                d -> getThreadPool(flowId).execute(() -> new FlowExecutor(data, ws).start(d)));
        // 过滤掉非起始节点的定时器节点
        List<FlowData> timers = filter(data, d -> isTimerNode(d) && isStart(d.getId(), data));
        forEach(
                timers,
                d -> getThreadPool(flowId).execute(() -> new FlowExecutor(data, ws).start(d)));
    }

    /** 停止流程 */
    public void stopFlowData(Long flowId) {
        FlowThreadPool.shutdownThreadPool(flowId);
        FlowThreadPool.shutdownSchedulerPool(flowId);
    }

    /** 被中断的节点需要推送中断状态信息 */
    public void sendNodeInterruptStatus(Long flowId, List<String> nodeIds) {
        Document status =
                new Document("status", Node.Status.FAILED.name())
                        .append("error", "node interrupted");
        forEach(
                nodeIds,
                nodeId -> ws.sendMessage(flowId.toString(), status.append("nodeId", nodeId)));
    }

    private boolean isStartNode(FlowData d) {
        return NodeTypeEnum.START.getType().equals(d.getType());
    }

    private boolean isTimerNode(FlowData d) {
        return NodeTypeEnum.TIMER.getType().equals(d.getType());
    }

    /** 判断当前节点是否为起始节点（即最上游节点） */
    private boolean isStart(String id, List<FlowData> list) {
        return list.stream().noneMatch(d -> id.equals(d.getTo()));
    }
}
