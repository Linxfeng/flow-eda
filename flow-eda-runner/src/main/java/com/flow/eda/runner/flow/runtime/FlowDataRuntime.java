package com.flow.eda.runner.flow.runtime;

import com.flow.eda.common.dubbo.model.FlowData;
import com.flow.eda.runner.flow.node.NodeTypeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.flow.eda.common.utils.CollectionUtil.*;

@Service
public class FlowDataRuntime {
    /** 用于单独执行各个流程的线程池 */
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    /** 存储所有启用状态的工作流数据 */
    private final Map<Long, List<FlowData>> flowData = new ConcurrentHashMap<>();
    /** 存储流程中的所有开始节点 */
    private final Map<Long, List<FlowData>> startData = new ConcurrentHashMap<>();
    /** 存储流程中的所有定时节点(作为起始节点) */
    private final Map<Long, List<FlowData>> timerData = new ConcurrentHashMap<>();

    /** 应用启动时，需要加载出正在运行中的流数据，继续运行 */
    @PostConstruct
    public void loadingFlowData() {}

    /** 存储流程的流节点数据 */
    public void putData(List<FlowData> data) {
        Long flowId = data.get(0).getFlowId();
        this.flowData.put(flowId, data);

        // 将流数据分为开始节点和定时器节点进行区分存放
        List<FlowData> startNodes = filter(data, this::isStartNode);
        if (isNotEmpty(startNodes)) {
            this.startData.put(flowId, startNodes);
        }
        // 过滤掉非起始节点的定时器节点
        List<FlowData> timerNodes = filter(data, d -> isTimerNode(d) && isStart(d.getId(), data));
        if (isNotEmpty(timerNodes)) {
            this.timerData.put(flowId, timerNodes);
        }
        // 存储完毕后需要触发定时器节点流程的执行(异步)
        threadPool.execute(() -> this.runTimerFlowData(flowId));
    }

    /** 执行指定流程的开始节点，仅执行一次 */
    public void runStartFlowData(List<FlowData> data) {
        this.putData(data);
        Long flowId = data.get(0).getFlowId();
        List<FlowData> starts = this.startData.get(flowId);
        forEach(starts, t -> threadPool.execute(() -> new FlowExecutor(data).start(t)));
    }

    /** 执行指定流程的定时器节点 */
    private void runTimerFlowData(Long flowId) {
        List<FlowData> timers = this.timerData.get(flowId);
        List<FlowData> data = this.flowData.get(flowId);
        forEach(timers, t -> threadPool.execute(() -> new FlowExecutor(data).start(t)));
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
