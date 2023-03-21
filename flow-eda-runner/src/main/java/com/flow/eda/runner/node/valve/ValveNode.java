package com.flow.eda.runner.node.valve;

import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import com.flow.eda.runner.utils.NodeParamsUtil;
import lombok.Getter;
import org.bson.Document;

import java.util.concurrent.atomic.AtomicInteger;

/** 阀门节点，控制流程中多条支路汇总时，运行的时间与次数 */
@Getter
public class ValveNode extends AbstractNode {
    private final AtomicInteger count = new AtomicInteger(0);
    private Integer times;
    private Long period;
    private NodeParamsUtil nodeParams;

    public ValveNode(Document params) {
        super(params);

        // 注册阀门节点，若注册失败，则节点状态置为空
        if (ValveNodeManager.register(this)) {
            this.nodeParams = new NodeParamsUtil(params).remove("times", "period");
        } else {
            setStatus(null);
        }
    }

    @Override
    public void run(NodeFunction callback) {
        if (this.nodeParams != null) {
            Document output = this.nodeParams.output(this).get();
            Runnable command =
                    () -> {
                        setStatus(Status.FINISHED);
                        callback.callback(output);
                    };
            ValveNodeManager.run(this, command);
        }
    }

    @Override
    protected void verify(Document params) {
        // 限制通过次数，默认1次
        this.times = 1;
        try {
            Object times = params.get("times");
            if (times != null) {
                this.times = Integer.parseInt(times.toString());
                NodeVerify.isTrue(this.times > 0, "times");
            }
        } catch (Exception e) {
            NodeVerify.throwWithName("times");
        }

        // 限制通过的固定周期，默认100ms
        this.period = 100L;
        try {
            Object period = params.get("period");
            if (period != null) {
                this.period = Long.parseLong(period.toString());
            }
        } catch (Exception e) {
            NodeVerify.throwWithName("period");
        }
    }

    /** 是否可以继续调用 */
    public synchronized boolean continueCall() {
        return this.count.get() < this.times;
    }

    /** 本次调用后，是否还可以继续调用 */
    public boolean call() {
        return this.count.incrementAndGet() < this.times;
    }
}
