package com.flow.eda.runner.flow.runtime;

import com.flow.eda.common.dubbo.model.FlowData;
import com.flow.eda.runner.flow.data.FlowWebSocket;
import com.flow.eda.runner.flow.node.NodeTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.flow.eda.common.utils.CollectionUtil.filter;
import static com.flow.eda.common.utils.CollectionUtil.forEach;

@Service
public class FlowDataRuntime {
    /** 用于单独执行各个流程的线程池 */
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    @Autowired private FlowWebSocket ws;

    /** 应用启动时，需要加载出正在运行中的流数据，继续运行 */
    @PostConstruct
    public void loadingFlowData() {}

    /** 运行流程 */
    public void runFlowData(List<FlowData> data) {
        // 将流数据分为开始节点和定时器节点进行分别执行
        List<FlowData> starts = filter(data, this::isStartNode);
        forEach(starts, d -> threadPool.execute(() -> new FlowExecutor(data, ws).start(d)));
        // 过滤掉非起始节点的定时器节点
        List<FlowData> timers = filter(data, d -> isTimerNode(d) && isStart(d.getId(), data));
        forEach(timers, d -> threadPool.execute(() -> new FlowExecutor(data, ws).start(d)));
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
