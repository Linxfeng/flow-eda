package com.flow.eda.runner.node;

/** 节点接口，用于约束所有的子类节点 */
public interface Node {
    /**
     * 运行当前节点实例
     *
     * @param callback 回调函数，当前节点执行完毕之后进行回调
     */
    void run(NodeFunction callback);

    /**
     * 获取当前节点的执行状态
     *
     * @return 节点状态，初始值为 Status.RUNNING
     */
    Status status();

    /** 流程运行时，节点的当前状态 */
    enum Status {
        /** 运行中 */
        RUNNING,
        /** 运行完成 */
        FINISHED,
        /** 运行失败 */
        FAILED
    }
}
