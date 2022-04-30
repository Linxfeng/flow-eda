package com.flow.eda.runner.node.delay;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import org.bson.Document;

import java.util.concurrent.TimeUnit;

import static com.flow.eda.runner.node.NodeVerify.isTrue;
import static com.flow.eda.runner.node.NodeVerify.notNull;

/** 延时器，延迟指定时间后继续执行 */
public class DelayNode extends AbstractNode {
    private Long delay;
    private TimeUnit unit;

    public DelayNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        try {
            System.out.println("执行延时器节点！");
            unit.sleep(delay);
            setStatus(Status.FINISHED);
            callback.callback(output());
        } catch (Exception e) {
            if (e.getMessage() != null) {
                throw FlowException.wrap(e, e.getMessage());
            }
            throw FlowException.wrap(e);
        }
    }

    @Override
    protected void verify(Document params) {
        try {
            notNull(params);
            String d = params.getString("delay");
            notNull(d);
            isTrue(d.contains(","));
            this.delay = Long.parseLong(d.split(",")[0]);
            this.unit = TimeUnit.valueOf(d.split(",")[1]);
            notNull(delay);
            notNull(unit);
            isTrue(delay > 0);
        } catch (Exception ignore) {
            NodeVerify.throwWithName("delay");
        }
    }
}
