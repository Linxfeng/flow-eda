package com.flow.eda.runner.node.http.receive;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import com.flow.eda.runner.node.http.HttpDispatcherServlet;
import com.flow.eda.runner.node.http.response.HttpResponseNode;
import com.flow.eda.runner.runtime.FlowBlockNodePool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/** HTTP接收节点，用于接收HTTP请求，解析出请求参数，向下游输出，与HTTP响应节点{@link HttpResponseNode}搭配使用 */
@Slf4j
public class HttpReceiveNode extends AbstractNode implements FlowBlockNodePool.BlockNode {
    private static final long TIMEOUT = 10_000;
    private String uri;
    private String method;
    private NodeFunction callback;
    private HttpServletResponse response;
    private volatile boolean finished = false;

    public HttpReceiveNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction function) {
        HttpDispatcherServlet.addHandlerMapping(this);
        setStatus(Status.RUNNING);
        this.callback = function;
    }

    /** 处理HTTP请求 */
    public void handle(HttpServletRequest request, HttpServletResponse response) {
        this.response = response;
        try {
            // 获取请求参数，向下游输出
            Document output = output();
            if (request.getInputStream() != null) {
                String body =
                        IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8.name());
                if (StringUtils.hasText(body)) {
                    output.putAll(Document.parse(body));
                }
            }
            String queryString = request.getQueryString();
            if (StringUtils.hasText(queryString)) {
                queryString = URLDecoder.decode(queryString, StandardCharsets.UTF_8.name());
                String[] params = queryString.split("&");
                for (String param : params) {
                    String[] kv = param.split("=");
                    output.append(kv[0], kv[1]);
                }
            }
            this.callback.callback(output);
        } catch (Exception e) {
            log.error("Parse request params failed: {}", e.getMessage());
            // 响应异常信息
            Document data =
                    new Document("error", "Parse request params failed")
                            .append("message", e.getMessage());
            this.responseData(data.toJson());
        }
        // 阻塞本方法，直至responseData被调用
        long now = System.currentTimeMillis();
        while (!this.finished) {
            try {
                TimeUnit.MILLISECONDS.sleep(5);
                // 超过设置的超时时间后返回请求超时
                if (System.currentTimeMillis() - now > TIMEOUT) {
                    throw new FlowException("Request timeout");
                }
            } catch (Exception e) {
                Document data =
                        new Document("error", "Request timeout").append("message", e.getMessage());
                this.responseData(data.toJson());
                break;
            }
        }
    }

    /** 响应HTTP请求，返回数据 */
    public void responseData(String data) {
        this.response.setContentType("application/json;charset=utf-8");
        try (PrintWriter writer = this.response.getWriter()) {
            writer.write(data);
            writer.flush();
        } catch (Exception e) {
            log.error("Write http response failed: {}", e.getMessage());
            throw new FlowException(e.getMessage());
        }
        this.finished = true;
    }

    @Override
    protected void verify(Document params) {
        this.uri = NodeVerify.requiredUrl(params, "uri");
        this.method = NodeVerify.requiredMethod(params);
    }

    public String getMapping() {
        return uri + ":" + method;
    }

    @Override
    public void destroy() {
        HttpDispatcherServlet.removeHandlerMapping(this);
    }
}
