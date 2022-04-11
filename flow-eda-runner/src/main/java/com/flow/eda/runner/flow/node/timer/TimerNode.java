package com.flow.eda.runner.flow.node.timer;

import com.flow.eda.runner.flow.node.Node;
import com.flow.eda.runner.flow.node.NodeFunction;
import org.bson.Document;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimerNode implements Node {
    private final long period;
    private final TimeUnit unit;
    private final int times;

    public TimerNode(Document params) {
        String pd = params.getString("period");
        this.period = Long.parseLong(pd.split(",")[0]);
        this.unit = TimeUnit.valueOf(pd.split(",")[1]);
        this.times = params.getInteger("times");
    }

    @Override
    public void run(NodeFunction function) {
        this.executeScheduledTask();
        function.callback(null);
    }

    private void executeScheduledTask() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        ScheduledFuture<?> future = executor.scheduleAtFixedRate(() -> {}, 0, period, unit);

        // future.cancel();
    }
}
