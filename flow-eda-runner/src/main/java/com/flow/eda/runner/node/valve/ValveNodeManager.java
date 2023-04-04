package com.flow.eda.runner.node.valve;

import com.flow.eda.runner.runtime.FlowThreadPool;
import com.flow.eda.runner.status.FlowStatusService;
import com.flow.eda.runner.utils.ApplicationContextUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** 阀门管理器，用于限制节点执行次数 */
public class ValveNodeManager {
    /** 管理阀门节点id对应的节点实例 */
    private static final Map<String, ValveNode> VALVE_NODE = new ConcurrentHashMap<>();

    /** 注册阀门节点 */
    public static boolean register(ValveNode node) {
        boolean success;
        String nodeId = node.getNodeId();
        synchronized (VALVE_NODE) {
            if (VALVE_NODE.containsKey(nodeId)) {
                success = VALVE_NODE.get(nodeId).continueCall();
            } else {
                VALVE_NODE.put(nodeId, node);
                success = true;
            }
        }
        if (success) {
            // 注册通知服务，流程运行结束后清理缓存
            FlowStatusService service = ApplicationContextUtil.getBean(FlowStatusService.class);
            service.registerNotice(node.getFlowId(), VALVE_NODE::remove);
        }
        return success;
    }

    /** 运行指定的阀门节点，若运行成功，则进行回调 */
    public static void run(ValveNode node, Runnable callback) {
        String nodeId = node.getNodeId();
        Runnable command =
                () -> {
                    // 判断是否可以执行
                    if (VALVE_NODE.containsKey(nodeId) && VALVE_NODE.get(nodeId).continueCall()) {
                        ValveNode valveNode = VALVE_NODE.get(nodeId);
                        // 先回调，再计时
                        boolean call = valveNode.call();
                        // 使用单独线程回调，避免计时不准确
                        FlowThreadPool.getThreadPool(node.getFlowId()).execute(callback);
                        try {
                            Thread.sleep(valveNode.getPeriod());
                        } catch (InterruptedException ignored) {
                        }
                        if (!call) {
                            VALVE_NODE.remove(nodeId);
                        }
                    }
                };
        FlowThreadPool.getThreadPool(node.getFlowId()).execute(command);
    }
}
