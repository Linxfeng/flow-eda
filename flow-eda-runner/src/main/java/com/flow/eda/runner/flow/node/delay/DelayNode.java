package com.flow.eda.runner.flow.node.delay;

import com.flow.eda.common.exception.InternalException;
import com.flow.eda.common.exception.InvalidParameterException;
import com.flow.eda.runner.flow.node.AbstractNode;
import com.flow.eda.runner.flow.node.NodeFunction;
import org.bson.Document;

import java.util.concurrent.TimeUnit;

import static com.flow.eda.runner.flow.node.NodeVerify.isTrue;
import static com.flow.eda.runner.flow.node.NodeVerify.notNull;

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
            throw new InternalException(e.getMessage());
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
            throw new InvalidParameterException("The parameter 'delay' is invalid");
        }
    }
}
