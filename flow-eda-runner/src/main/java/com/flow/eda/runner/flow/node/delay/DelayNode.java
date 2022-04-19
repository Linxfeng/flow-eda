package com.flow.eda.runner.flow.node.delay;

import com.flow.eda.runner.flow.node.AbstractNode;
import com.flow.eda.runner.flow.node.NodeFunction;
import org.bson.Document;

import java.util.concurrent.TimeUnit;

/** 延时器，延迟指定时间后继续执行 */
public class DelayNode extends AbstractNode {
    private final long delay;
    private final TimeUnit unit;

    public DelayNode(Document params) {
        super(params);
        String d = params.getString("delay");
        this.delay = Long.parseLong(d.split(",")[0]);
        this.unit = TimeUnit.valueOf(d.split(",")[1]);
    }

    @Override
    public void run(NodeFunction callback) {
        try {
            System.out.println("执行延时器节点！");
            unit.sleep(delay);
            callback.callback(output());
        } catch (InterruptedException ignored) {
        }
    }
}
