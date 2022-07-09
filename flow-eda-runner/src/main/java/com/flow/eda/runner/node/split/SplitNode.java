package com.flow.eda.runner.node.split;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SplitNode extends AbstractNode {
    private boolean outputArray;
    private List<String> result;

    public SplitNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        Document output = output();
        if (this.outputArray) {
            setStatus(Status.FINISHED);
            callback.callback(output.append("result", this.result));
            return;
        }
        for (int i = 0; i < this.result.size(); i++) {
            try {
                // 每次输出前等待100ms，避免输出太快导致数据丢失
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ignored) {
            }
            if (i == this.result.size() - 1) {
                setStatus(Status.FINISHED);
            }
            callback.callback(output.append("result", this.result.get(i)));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void verify(Document params) {
        String field = params.getString("field");
        NodeVerify.notNull(field, "field");

        String separator = ",";
        String sp = params.getString("separator");
        if (sp != null) {
            separator = sp;
        }

        String outputWay = params.getString("outputWay");
        this.outputArray = !"多次输出单值".equals(outputWay);

        // 获取字段值并进行分割
        this.result = new ArrayList<>();
        try {
            Object value = params.get(field);
            if (field.startsWith("params.")) {
                value = getInput().get(field.substring("params.".length()));
            }
            if (value == null) {
                this.result.add("");
            } else if (value instanceof Collection) {
                this.result =
                        ((Collection<Object>) value)
                                .stream().map(Object::toString).collect(Collectors.toList());
            } else {
                this.result = Arrays.asList(value.toString().split(separator));
            }
        } catch (Exception e) {
            throw new FlowException(e.getMessage());
        }
    }
}
