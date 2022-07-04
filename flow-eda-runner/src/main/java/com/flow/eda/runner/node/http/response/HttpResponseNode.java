package com.flow.eda.runner.node.http.response;

import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import com.flow.eda.runner.node.http.HttpDispatcherServlet;
import com.flow.eda.runner.node.http.receive.HttpReceiveNode;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.util.StringUtils;

/** HTTP响应节点，用于响应HTTP请求，与HTTP接收节点{@link HttpReceiveNode}搭配使用 */
@Slf4j
public class HttpResponseNode extends AbstractNode {
    private String uri;
    private String method;
    private Document resData;
    private Document output;

    public HttpResponseNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction function) {
        // 响应数据
        HttpDispatcherServlet.responseData(getMapping(), output.toJson());
        // 向下输出
        setStatus(Status.FINISHED);
        Document out = output();
        out.putAll(this.output);
        function.callback(out);
    }

    @Override
    protected void verify(Document params) {
        this.uri = NodeVerify.requiredUrl(params, "uri");
        this.method = NodeVerify.requiredMethod(params);

        String res = params.getString("resData");
        if (StringUtils.hasText(res)) {
            res = res.trim();
            NodeVerify.isTrue(res.startsWith("{"), "resData");
            NodeVerify.isTrue(res.endsWith("}"), "resData");
            try {
                this.resData = Document.parse(res);
            } catch (Exception ignored) {
                NodeVerify.throwWithName("resData");
            }
            this.output = this.resData;
        } else {
            this.output = new Document();
            this.output.putAll(params);
            this.output.remove("uri");
            this.output.remove("method");
            this.output.remove("resData");
        }
    }

    public String getMapping() {
        return uri + ":" + method;
    }
}
