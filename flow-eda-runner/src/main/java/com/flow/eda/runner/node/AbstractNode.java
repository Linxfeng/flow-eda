package com.flow.eda.runner.node;

import com.flow.eda.runner.utils.PlaceholderUtil;
import org.bson.Document;

import java.util.Optional;

/** 节点抽象类 */
public abstract class AbstractNode implements Node {
    /** 输入参数，由上个节点传递至此 */
    private final Document input;

    private final String flowId;
    private final String nodeId;

    /** 节点自定义参数，可传递至下个节点 */
    private Document payload;
    /** 节点当前的运行状态 */
    private Status status = Status.RUNNING;

    /**
     * 抽象类含参构造，用于约束子类必须有一个含参构造
     *
     * @param params 节点的输入参数，由上一个节点传递至此
     */
    public AbstractNode(Document params) {
        NodeVerify.notNull(params, "params");
        this.flowId = params.getString("flowId");
        this.nodeId = params.getString("nodeId");
        this.payload = params.get("payload", Document.class);
        this.input = params.get("input", Document.class);
        params.remove("flowId");
        params.remove("nodeId");
        // 解析${}占位符
        params = this.parsePlaceholder(params);
        // 校验节点参数
        this.verify(params);
    }

    /** 解析${}占位符，取值并进行填充 */
    protected Document parsePlaceholder(Document params) {
        // 从上个节点的输出参数中取值
        if (input != null) {
            payload = PlaceholderUtil.replacePlaceholder(payload, input);
            params = PlaceholderUtil.replacePlaceholder(params, input);
        }
        // 从本节点的输入参数中取值
        if (params != null) {
            payload = PlaceholderUtil.replacePlaceholder(payload, params);
        }
        return params;
    }

    /** 节点参数校验 */
    protected abstract void verify(Document params);

    @Override
    public Status status() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Document getInput() {
        return Optional.ofNullable(input).orElseGet(Document::new);
    }

    /** 节点输出：当前节点的payload作为下一个节点的input */
    public Document output() {
        Optional<Document> optional = Optional.ofNullable(this.payload);
        return optional.map(p -> new Document("input", p)).orElseGet(Document::new);
    }

    public String getFlowId() {
        return flowId;
    }

    public String getNodeId() {
        return nodeId;
    }
}
