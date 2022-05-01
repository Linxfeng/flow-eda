package com.flow.eda.web.flow.status;

import com.flow.eda.web.log.LogAspect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/** 看门狗，用于监听流程状态变化 */
public class FlowStatusWatchDog {
    /** 刷新周期 */
    private static final long SLEEP = 2000;
    /** key为流程id，流程状态更新时会更新对应的value值为true */
    private static final Map<String, Boolean> MAP = new ConcurrentHashMap<>();

    /** 刷新看门狗 */
    public static void refresh(String flowId, Consumer<String> callback) {
        if (MAP.isEmpty()) {
            MAP.put(flowId, true);
            watch(callback);
        } else {
            MAP.put(flowId, true);
        }
    }

    /** 监视对应的流程是否进行刷新，若固定周期内未刷新，则踢出map并进行回调 */
    private static void watch(Consumer<String> callback) {
        Runnable runnable =
                () -> {
                    while (!MAP.isEmpty()) {
                        MAP.keySet()
                                .forEach(
                                        flowId -> {
                                            if (MAP.get(flowId)) {
                                                MAP.put(flowId, false);
                                            }
                                        });
                        try {
                            Thread.sleep(SLEEP);
                        } catch (InterruptedException e) {
                            LogAspect.error(e.getMessage());
                        }
                        MAP.keySet()
                                .forEach(
                                        flowId -> {
                                            if (!MAP.get(flowId)) {
                                                MAP.remove(flowId);
                                                callback.accept(flowId);
                                            }
                                        });
                    }
                };
        // 显式创建单独线程去执行监视任务，若任务执行完毕，此线程将被销毁
        new Thread(runnable).start();
    }
}
