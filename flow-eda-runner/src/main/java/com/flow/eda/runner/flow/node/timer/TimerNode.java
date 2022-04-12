package com.flow.eda.runner.flow.node.timer;

import com.flow.eda.runner.flow.node.Node;
import com.flow.eda.runner.flow.node.NodeFunction;
import org.bson.Document;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/** 定时器节点：作为起始节点时，将会定时触发执行 */
public class TimerNode implements Node {
    private final long period;
    private final TimeUnit unit;
    private final int times;

    public TimerNode(Document params) {
        String pd = params.getString("period");
        this.period = Long.parseLong(pd.split(",")[0]);
        this.unit = TimeUnit.valueOf(pd.split(",")[1]);
        this.times = Integer.parseInt(params.getString("times"));
    }

    @Override
    public void run(NodeFunction function) {
        this.executeScheduledTask(function);
    }

    /** 执行单个定时器节点，每个节点使用单独的定时线程池 */
    private void executeScheduledTask(NodeFunction function) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        if (this.times > 0) {
            AtomicInteger n = new AtomicInteger();
            // 执行times次数后，停止
            executor.scheduleAtFixedRate(
                    () -> {
                        if (n.getAndIncrement() < times) {
                            function.callback(null);
                        } else {
                            // 停止当前定时线程
                            executor.shutdown();
                        }
                    },
                    0,
                    period,
                    unit);
        } else {
            // 永久循环，不限制次数
            executor.scheduleAtFixedRate(() -> function.callback(null), 0, period, unit);
        }
    }
}
