package com.flow.eda.runner.node.splice;

import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/** 拼接节点，接收上游节点的输出参数，对指定字段的进行取值拼接，默认等待固定周期后未更新值则运行结束并输出结果 */
public class SpliceNode extends AbstractNode {
    private String value;
    private List<String> result;
    private NodeFunction callback;

    public SpliceNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        this.callback = callback;
        if (value != null) {
            SpliceNodeManager.add(this);
        }
    }

    public String getValue() {
        return value;
    }

    public int getSize() {
        return result == null ? 0 : result.size();
    }

    public void addValue(String value) {
        if (this.result == null) {
            this.result = new ArrayList<>();
        }
        this.result.add(value);
    }

    public void callback() {
        String result = String.join(",", this.result);
        SpliceNodeManager.remove(getNodeId());
        setStatus(Status.FINISHED);
        callback.callback(output().append("result", result));
    }

    @Override
    protected void verify(Document params) {
        String field = params.getString("field");
        NodeVerify.notBlank(field, "field");

        String filter = params.getString("filter");
        boolean filterNull = true;
        if (filter != null) {
            filterNull = !"不过滤".equals(filter);
        }

        // 获取字段值
        Object value;
        if (field.startsWith("params.")) {
            value = getInput().get(field.substring("params.".length()));
        } else {
            value = params.get(field);
        }
        if (value != null) {
            this.value = value.toString();
        } else if (!filterNull) {
            this.value = "null";
        }
    }
}
