package com.flow.eda.runner.flow.node;

import org.bson.Document;

@FunctionalInterface
public interface NodeFunction {

    /**
     * 回调函数，用于节点执行完毕后对下一个节点进行回调
     *
     * @param params 当前节点的输出参数作为下一个节点的输入参数
     */
    void callback(Document params);
}
