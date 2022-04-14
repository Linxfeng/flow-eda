package com.flow.eda.runner.flow.node;

/** 节点接口，用于约束所有的子类节点 */
public interface Node {
    /**
     * 运行当前节点实例
     *
     * @param callback 回调函数，当前节点执行完毕之后进行回调
     */
    void run(NodeFunction callback);
}
