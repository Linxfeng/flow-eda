package com.flow.eda.runner.flow.node;

import org.bson.Document;

import java.util.Optional;

/** 节点抽象类 */
public abstract class AbstractNode implements Node {
    /** 节点自定义参数，可传递至下个节点 */
    private final Document payload;
    /** 输入参数，由上个节点传递至此 */
    private final Document input;
    /** 节点当前的运行状态 */
    private Status status = Status.RUNNING;

    /**
     * 抽象类含参构造，用于约束子类必须有一个含参构造
     *
     * @param params 节点的输入参数，由上一个节点传递至此
     */
    public AbstractNode(Document params) {
        if (params != null) {
            this.payload = params.get("payload", Document.class);
            this.input = params.get("input", Document.class);
        } else {
            this.payload = null;
            this.input = null;
        }
        this.verify(params);
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
}
