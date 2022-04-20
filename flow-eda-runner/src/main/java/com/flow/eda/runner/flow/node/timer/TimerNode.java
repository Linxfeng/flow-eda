package com.flow.eda.runner.flow.node.timer;

import com.flow.eda.runner.flow.node.AbstractNode;
import com.flow.eda.runner.flow.node.NodeFunction;
import org.bson.Document;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/** 定时器节点：作为起始节点时，将会定时触发执行 */
public class TimerNode extends AbstractNode {
    private final long period;
    private final TimeUnit unit;
    private final int times;

    public TimerNode(Document params) {
        super(params);
        this.times = Integer.parseInt(params.getString("times"));
        String pd = params.getString("period");
        this.period = Long.parseLong(pd.split(",")[0]);
        this.unit = TimeUnit.valueOf(pd.split(",")[1]);
    }

    @Override
    public void run(NodeFunction function) {
        this.executeScheduledTask(function);
    }

    /** 执行单个定时器节点，每个节点使用单独的定时线程池 */
    private void executeScheduledTask(NodeFunction function) {
        AtomicInteger n = new AtomicInteger();
        // 执行times次数后，停止
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        if (this.times > 0) {
            executor.scheduleAtFixedRate(
                    () -> {
                        if (n.getAndIncrement() < times) {
                            System.out.println("执行定时器节点！");
                            function.callback(output());
                        } else {
                            // 停止当前定时线程
                            executor.shutdown();
                            setStatus(Status.FINISHED);
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
