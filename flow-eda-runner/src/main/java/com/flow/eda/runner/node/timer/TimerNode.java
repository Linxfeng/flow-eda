package com.flow.eda.runner.node.timer;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.common.exception.InvalidParameterException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import lombok.Getter;
import org.bson.Document;
import org.springframework.scheduling.support.CronExpression;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/** 定时器节点：作为起始节点时，将会定时触发执行 */
@Getter
public class TimerNode extends AbstractNode {
    private Long period;
    private TimeUnit unit;
    private Integer times;
    private String cron;
    private String timePattern;
    private DateFormat dateFormat;
    private Runnable runnable;

    public TimerNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        this.runnable =
                () -> {
                    Document output = super.output();
                    // 输出指定格式的时间戳
                    if (this.timePattern != null) {
                        Object timestamp;
                        if (dateFormat != null) {
                            timestamp = dateFormat.format(new Date());
                        } else {
                            timestamp = Instant.now().toEpochMilli();
                        }
                        output.append("timestamp", timestamp);
                    }
                    callback.callback(output);
                };
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
            NodeVerify.notNull(params, "times");
            NodeVerify.isTrue(params.containsKey("times"), "times");
            this.times = Integer.parseInt(params.getString("times"));
            NodeVerify.isTrue(times >= 0, "times");

            String pd = params.getString("period");
            NodeVerify.notNull(pd, "period");
            NodeVerify.isTrue(pd.contains(","), "period");

            String type = pd.substring(pd.lastIndexOf(",") + 1);
            String value = pd.substring(0, pd.lastIndexOf(","));

            // 解析cron表达式
            if ("CRON".equals(type)) {
                if (!CronExpression.isValidExpression(value)) {
                    throw new InvalidParameterException("The parameter cron expression is invalid");
                }
                this.cron = value;
            } else {
                this.period = Long.parseLong(value);
                NodeVerify.isTrue(period > 0, "period");
                this.unit = TimeUnit.valueOf(type);
                NodeVerify.notNull(unit, "period");
            }

            if (params.containsKey("timestamp")) {
                String pattern = params.getString("timestamp");
                if (!"timestamp".equals(pattern)) {
                    this.checkPattern(pattern);
                }
                this.timePattern = pattern;
            }
        } catch (Exception e) {
            throw FlowException.wrap(e, "The timer node parameters is invalid");
        }
    }

    private void checkPattern(String pattern) {
        try {
            this.dateFormat = new SimpleDateFormat(pattern);
            this.dateFormat.format(new Date());
        } catch (Exception e) {
            NodeVerify.throwWithName("timestamp");
        }
    }
}
