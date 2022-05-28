package com.flow.eda.runner.node.ws.server;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Getter
public class WsServerNode extends AbstractNode {
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
        WsServerNodeManager.addEndpoint(this);

        // 上游节点输出参数发送
        Document input = getInput();
        if (!input.isEmpty()) {
            output.append("params", input);
        }
        output.putAll(output());
        output.remove("input");
        output.remove("payload");
        WsNodeHandler.sendMessage(path, output.toJson());
    }

    @Override
    protected void verify(Document params) {
        // 校验路径参数，不支持通配符
        this.path = NodeVerify.requiredUrl(params, "path");
        if (path.contains("{") || path.contains("}")) {
            throw new FlowException("The parameter 'path' can not contains symbols '{' or '}'");
        }
        // set output
        this.output = new Document();
        this.output.putAll(params);
        this.output.remove("path");

        String q = params.getString("query");
        if (StringUtils.hasLength(q)) {
            this.query = Arrays.asList(q.split(","));
        }
        this.output.remove("query");

        this.sendAfterConnect = params.getString("sendAfterConnect");
        this.output.remove("sendAfterConnect");

        this.sendAfterReceive = params.getString("sendAfterReceive");
        this.output.remove("sendAfterReceive");

        String isSend = params.getString("fanout");
        if ("Send".equals(isSend)) {
            this.fanout = true;
        }
        this.output.remove("fanout");
    }

    public void callback(String message) {
        this.callback.callback(output().append("message", message));
    }
}
