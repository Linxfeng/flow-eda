package com.flow.eda.runner.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** 用于统一管理流程中的所有阻塞节点 */
public class FlowBlockNodePool {
    private static final Map<String, List<BlockNode>> POOL = new ConcurrentHashMap<>();

    public static void addBlockNode(String flowId, BlockNode blockNode) {
        if (POOL.containsKey(flowId)) {
            List<BlockNode> list = POOL.get(flowId);
            // 若判断两个阻塞节点相同，需要进行替换更新
            list.removeIf(node -> node.eq(blockNode));
            list.add(blockNode);
        } else {
            List<BlockNode> list = new ArrayList<>();
            list.add(blockNode);
            POOL.put(flowId, list);
        }
    }

    public static void shutdownBlockNode(String flowId) {
        if (POOL.containsKey(flowId)) {
            POOL.get(flowId).forEach(BlockNode::destroy);
            POOL.remove(flowId);
        }
    }

    /** 阻塞节点需要实现此接口，重写销毁方法 */
    public interface BlockNode {
        void destroy();

        /** 提供比较两个阻塞节点是否相同的扩展方法 */
        default boolean eq(BlockNode blockNode) {
            return false;
        }
    }
}
