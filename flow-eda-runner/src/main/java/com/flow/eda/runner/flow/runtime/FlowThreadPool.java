package com.flow.eda.runner.flow.runtime;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** 用于执行整个流程的线程池管理工具,方便统一管理和约束线程池的创建/使用/销毁 */
public class FlowThreadPool {
    private static final Map<Long, ExecutorService> POOL_MAP = new ConcurrentHashMap<>();
    private static final Map<Long, List<ThreadPoolTaskScheduler>> SCHEDULER_POOL_MAP =
            new ConcurrentHashMap<>();

    public static ExecutorService getThreadPool(Long flowId) {
        if (POOL_MAP.containsKey(flowId)) {
            return POOL_MAP.get(flowId);
        }
        ExecutorService threadPool = Executors.newCachedThreadPool();
        POOL_MAP.put(flowId, threadPool);
        return threadPool;
    }

    public static void shutdownThreadPool(Long flowId) {
        if (POOL_MAP.containsKey(flowId)) {
            POOL_MAP.get(flowId).shutdownNow();
            POOL_MAP.remove(flowId);
        }
    }

    public static ThreadPoolTaskScheduler getSchedulerPool(Long flowId) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.setThreadNamePrefix("scheduledTask-");
        threadPoolTaskScheduler.initialize();
        if (flowId != null) {
            if (SCHEDULER_POOL_MAP.containsKey(flowId)) {
                SCHEDULER_POOL_MAP.get(flowId).add(threadPoolTaskScheduler);
            } else {
                List<ThreadPoolTaskScheduler> list = new ArrayList<>();
                list.add(threadPoolTaskScheduler);
                SCHEDULER_POOL_MAP.put(flowId, list);
            }
        }
        return threadPoolTaskScheduler;
    }

    public static void shutdownSchedulerPool(Long flowId) {
        if (SCHEDULER_POOL_MAP.containsKey(flowId)) {
            SCHEDULER_POOL_MAP.get(flowId).forEach(ThreadPoolTaskScheduler::shutdown);
            SCHEDULER_POOL_MAP.remove(flowId);
        }
    }
}
