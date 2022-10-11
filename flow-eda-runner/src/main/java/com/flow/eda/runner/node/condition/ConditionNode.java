package com.flow.eda.runner.node.condition;

import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import com.flow.eda.runner.utils.PlaceholderUtil;
import org.bson.Document;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** 条件节点，可使用多个条件节点进行组合使用，形成与或逻辑 */
public class ConditionNode extends AbstractNode {
    private String field;
    private String condition;
    private String value;
    private Boolean passed;
    private Document output;

    public ConditionNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        if (this.passed) {
            Document input = getInput();
            if (!input.isEmpty()) {
                output.append("params", input);
            }
            output.putAll(output());
            if (output.containsKey("input")) {
                output.put("params", output.get("input"));
            }
            output.remove("input");
            output.remove("payload");

            setStatus(Status.FINISHED);
            callback.callback(output);
        }
    }

    @Override
    protected void verify(Document params) {
        this.field = params.getString("field");
        NodeVerify.notNull(this.field, "field");
        if (!getInput().isEmpty()) {
            this.field = this.field.replace("params.", "input.");
        }

        this.condition = params.getString("condition");
        NodeVerify.notNull(this.condition, "condition");

        this.value = params.getString("value");
        NodeVerify.notNull(this.value, "value");

        this.passed = this.meetConditions(params);
        if (this.passed) {
            this.output = new Document();
            this.output.putAll(params);
            this.output.remove("field");
            this.output.remove("condition");
            this.output.remove("value");
        } else {
            // 条件未通过时，节点状态置为空
            setStatus(null);
        }
    }

    /** 解析条件表达式，返回条件判断结果 */
    private boolean meetConditions(Document params) {
        try {
            Document result = PlaceholderUtil.parse(params, Collections.singletonList(this.field));
            return this.dealConditions(result.get(this.field));
        } catch (Exception ignored) {
            return false;
        }
    }

    /** 处理判断条件 */
    private boolean dealConditions(Object data) {
        List<String> list = Arrays.asList("null", "NULL", "Null");
        String equals = "=";
        String notEquals = "!=";
        if (list.contains(this.value)) {
            if (equals.equals(this.condition)) {
                return data == null;
            }
            if (notEquals.equals(this.condition)) {
                return data != null;
            }
            return false;
        }
        if (data == null) {
            return notEquals.equals(this.condition);
        }
        switch (this.condition) {
            case "=":
                return this.value.equals(data.toString());
            case "!=":
                return !this.value.equals(data.toString());
            case ">":
                return Double.parseDouble(data.toString()) > Double.parseDouble(this.value);
            case "<":
                return Double.parseDouble(data.toString()) < Double.parseDouble(this.value);
            case ">=":
                return Double.parseDouble(data.toString()) >= Double.parseDouble(this.value);
            case "<=":
                return Double.parseDouble(data.toString()) <= Double.parseDouble(this.value);
            default:
                return false;
        }
    }
}
