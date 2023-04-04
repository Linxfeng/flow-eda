package com.flow.eda.runner.node.delay;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import com.flow.eda.runner.utils.NodeParamsUtil;
import org.bson.Document;

import java.util.concurrent.TimeUnit;

/** 延时器，延迟指定时间后继续执行 */
public class DelayNode extends AbstractNode {
    private Long delay;
    private TimeUnit unit;
    private NodeParamsUtil nodeParams;

    public DelayNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        try {
            unit.sleep(delay);
            setStatus(Status.FINISHED);
            callback.callback(this.nodeParams.output(this).get());
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
            String d = params.getString("delay");
            NodeVerify.notNull(d);

            NodeVerify.isTrue(d.contains(","));
            this.delay = Long.parseLong(d.split(",")[0]);

            NodeVerify.notNull(delay);
            NodeVerify.isTrue(delay > 0);

            this.unit = TimeUnit.valueOf(d.split(",")[1]);
            NodeVerify.notNull(unit);

            this.nodeParams = new NodeParamsUtil(params).remove("delay");
        } catch (Exception ignore) {
            NodeVerify.throwWithName("delay");
        }
    }
}
