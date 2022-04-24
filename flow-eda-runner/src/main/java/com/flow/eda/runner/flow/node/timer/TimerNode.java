package com.flow.eda.runner.flow.node.timer;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.common.exception.InvalidParameterException;
import com.flow.eda.runner.flow.node.AbstractNode;
import com.flow.eda.runner.flow.node.NodeFunction;
import lombok.Getter;
import org.bson.Document;
import org.springframework.scheduling.support.CronExpression;

import java.util.concurrent.TimeUnit;

import static com.flow.eda.runner.flow.node.NodeVerify.isTrue;
import static com.flow.eda.runner.flow.node.NodeVerify.notNull;

/** 定时器节点：作为起始节点时，将会定时触发执行 */
@Getter
public class TimerNode extends AbstractNode {
    private Long flowId;
    private Long period;
    private TimeUnit unit;
    private Integer times;
    private String cron;
    private Runnable runnable;

    public TimerNode(Document params) {
        super(params);
        String name = Thread.currentThread().getName();
        if (name.startsWith("flowId:")) {
            this.flowId = Long.parseLong(name.split(":")[1]);
        }
    }

    @Override
    public void run(NodeFunction callback) {
        this.runnable = () -> callback.callback(output());
        // 执行指定次数后，需要停止任务并设置节点状态
        if (this.times > 0) {
            TimerTask.runTimes(this, () -> setStatus(Status.FINISHED));
        } else {
            TimerTask.run(this);
        }
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

            // 解析cron表达式
            if ("CRON".equals(pd.split(",")[1])) {
                String cron = pd.split(",")[0];
                if (!CronExpression.isValidExpression(cron)) {
                    throw new InvalidParameterException("The parameter cron expression is invalid");
                }
                this.cron = cron;
            } else {
                this.period = Long.parseLong(pd.split(",")[0]);
                isTrue(period > 0, "period");
                this.unit = TimeUnit.valueOf(pd.split(",")[1]);
                notNull(unit, "period");
            }
        } catch (Exception e) {
            throw FlowException.wrap(e, "The timer node parameters is invalid");
        }
    }
}
