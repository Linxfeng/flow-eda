package com.flow.eda.runner.node.sequence;

import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import org.bson.Document;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

public class SequenceNode extends AbstractNode {
    private int start;
    private int end;
    private int step;
    private boolean increase;

    public SequenceNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        setStatus(Status.RUNNING);
        int i = this.start;
        while (this.increase ? i <= this.end : i >= this.end) {
            Document output = output().append("index", i);
            if (this.increase) {
                i += this.step;
            } else {
                i -= this.step;
            }
            if (this.increase ? i >= this.end : i <= this.end) {
                setStatus(Status.FINISHED);
            }
            try {
                // 每次输出前等待100ms，避免输出太快导致数据丢失
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ignored) {
            }
            callback.callback(output);
        }
    }

    @Override
    protected void verify(Document params) {
        this.start = this.parseInt(params, "start");
        this.end = this.parseInt(params, "end");

        String step = params.getString("step");
        if (StringUtils.hasText(step)) {
            this.step = this.parseInt(params, "step");
            NodeVerify.isTrue(this.step > 0, "step");
        } else {
            this.step = 1;
        }

        String action = params.getString("action");
        this.increase = !"递减".equals(action);
    }

    private int parseInt(Document params, String str) {
        String s = params.getString(str);
        NodeVerify.notNull(s, str);
        try {
            return Integer.parseInt(s);
        } catch (Exception ignored) {
            NodeVerify.throwWithName(str);
        }
        return 0;
    }
}
