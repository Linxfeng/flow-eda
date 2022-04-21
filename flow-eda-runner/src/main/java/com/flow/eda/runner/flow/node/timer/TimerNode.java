package com.flow.eda.runner.flow.node.timer;

import com.flow.eda.common.exception.InvalidParameterException;
import com.flow.eda.runner.flow.node.AbstractNode;
import com.flow.eda.runner.flow.node.NodeFunction;
import org.bson.Document;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.flow.eda.runner.flow.node.NodeVerify.isTrue;
import static com.flow.eda.runner.flow.node.NodeVerify.notNull;

/** 定时器节点：作为起始节点时，将会定时触发执行 */
public class TimerNode extends AbstractNode {
    private Long period;
    private TimeUnit unit;
    private Integer times;

    public TimerNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction function) {
        this.executeScheduledTask(function);
    }

    @Override
    protected void verify(Document params) {
        try {
            notNull(params, "times");
            isTrue(params.containsKey("times"), "times");
            this.times = Integer.parseInt(params.getString("times"));
            isTrue(times >= 0, "times");

            String pd = params.getString("period");
            notNull(pd, "period");
            isTrue(pd.contains(","), "period");
            this.period = Long.parseLong(pd.split(",")[0]);
            isTrue(period > 0, "period");

            this.unit = TimeUnit.valueOf(pd.split(",")[1]);
            notNull(unit, "period");
        } catch (InvalidParameterException e) {
            throw e;
        } catch (Exception ignore) {
            throw new InvalidParameterException("The timer node parameters is invalid");
        }
    }

    /** 执行单个定时器节点，每个节点使用单独的定时线程池 */
    private void executeScheduledTask(NodeFunction function) {
        // 执行times次数后，停止
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        AtomicInteger n = new AtomicInteger();
        if (this.times > 0) {
            Runnable command =
                    () -> {
                        if (n.getAndIncrement() < times) {
                            System.out.println("执行定时器节点！");
                            // 最后一次回调时，需要设置运行状态
                            if (n.get() == times) {
                                setStatus(Status.FINISHED);
                            }
                            function.callback(output());
                        } else {
                            // 停止当前定时线程
                            executor.shutdown();
                        }
                    };
            executor.scheduleAtFixedRate(command, 0, period, unit);
        } else {
            // 永久循环，不限制次数
            executor.scheduleAtFixedRate(() -> function.callback(null), 0, period, unit);
        }
    }
}
