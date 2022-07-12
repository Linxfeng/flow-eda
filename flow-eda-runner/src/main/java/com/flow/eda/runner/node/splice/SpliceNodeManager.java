package com.flow.eda.runner.node.splice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/** 管理拼接节点，默认等待200ms未更新则运行结束并回调 */
public class SpliceNodeManager {
    /** 等待周期 */
    private static final long SLEEP = 200;
    /** 节点id对应的节点实例 */
    private static final Map<String, SpliceNode> SPLICE_NODE_MAP = new ConcurrentHashMap<>();

    public static void add(SpliceNode spliceNode) {
        String nodeId = spliceNode.getNodeId();
        if (SPLICE_NODE_MAP.containsKey(nodeId)) {
            SPLICE_NODE_MAP.get(nodeId).addValue(spliceNode.getValue());
        } else {
            spliceNode.addValue(spliceNode.getValue());
            SPLICE_NODE_MAP.put(nodeId, spliceNode);
            // 为该节点添加监视器
            watch(nodeId);
        }
    }

    public static void remove(String nodeId) {
        SPLICE_NODE_MAP.remove(nodeId);
    }

    /** 监视节点值更新，超过固定周期未更新直接结束 */
    private static void watch(String nodeId) {
        Runnable runnable =
                () -> {
                    int size;
                    do {
                        size = SPLICE_NODE_MAP.get(nodeId).getSize();
                        try {
                            TimeUnit.MILLISECONDS.sleep(SLEEP);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } while (size != SPLICE_NODE_MAP.get(nodeId).getSize());
                    // 回调
                    SPLICE_NODE_MAP.get(nodeId).callback();
                };
        new Thread(runnable).start();
    }
}
