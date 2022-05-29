package com.flow.eda.runner.node.ws.server;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import com.flow.eda.runner.runtime.FlowBlockNodePool;
import lombok.Getter;
import org.bson.Document;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/** WebSocket服务端节点 */
@Getter
public class WsServerNode extends AbstractNode implements FlowBlockNodePool.BlockNode {
    private String path;
    private List<String> query;
    private String sendAfterConnect;
    private String sendAfterReceive;
    private boolean fanout = false;
    private NodeFunction callback;
    private Document output;

    public WsServerNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        setStatus(Status.RUNNING);
        this.callback = callback;

        // 添加ws服务端点，若存在同一节点，则进行更新替换
        WsServerNodeManager.addEndpoint(this);

        // 上游节点输出参数发送
        if (this.fanout) {
            Document input = getInput();
            if (!input.isEmpty()) {
                output.append("params", input);
            }
            output.putAll(output());
            output.remove("input");
            output.remove("payload");
            // 根据path路径广播发送
            WsServerNodeHandler.sendMessage(path, output.toJson());
        }
    }

    @Override
    protected void verify(Document params) {
        // 校验路径参数，不支持通配符
        this.path = NodeVerify.requiredUrl(params, "path");
        if (path.contains("{") || path.contains("}")) {
            throw new FlowException("The parameter 'path' can not contains symbols '{' or '}'");
        }

        String q = params.getString("query");
        if (StringUtils.hasLength(q)) {
            this.query = Arrays.asList(q.split(","));
        }

        this.sendAfterConnect = params.getString("sendAfterConnect");
        this.sendAfterReceive = params.getString("sendAfterReceive");

        String isSend = params.getString("fanout");
        if ("Send".equals(isSend)) {
            this.fanout = true;
        }

        // set output
        if (this.fanout) {
            this.output = new Document();
            this.output.putAll(params);
            this.output.remove("path");
            this.output.remove("query");
            this.output.remove("sendAfterConnect");
            this.output.remove("sendAfterReceive");
            this.output.remove("fanout");
        }
    }

    /** 回调，收到消息后向下游节点输出 */
    public void callback(String message) {
        this.callback.callback(output().append("message", message));
    }

    @Override
    public void destroy() {
        // 移除ws端点
        WsServerNodeManager.removeEndpoint(this);
        // 关闭ws连接
        WsServerNodeHandler.onClose(this.path);
    }

    @Override
    public boolean eq(FlowBlockNodePool.BlockNode blockNode) {
        // 根据path路径判断两个节点是否相同
        if (blockNode instanceof WsServerNode) {
            WsServerNode node = (WsServerNode) blockNode;
            return this.path.equals(node.getPath());
        }
        return false;
    }
}
