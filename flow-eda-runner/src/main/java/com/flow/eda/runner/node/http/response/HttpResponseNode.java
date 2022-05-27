package com.flow.eda.runner.node.http.response;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.common.utils.CollectionUtil;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import com.flow.eda.runner.node.http.request.HttpRequestNode;
import com.flow.eda.runner.runtime.FlowBlockNodePool;
import com.flow.eda.runner.utils.PlaceholderUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.flow.eda.common.utils.CollectionUtil.isNotEmpty;

@Slf4j
public class HttpResponseNode extends AbstractNode implements FlowBlockNodePool.BlockNode {
    private String uri;
    private String method;
    private List<String> query;
    private Document resData;

    public HttpResponseNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction function) {
        HttpResponseServlet.addHandlerMapping(this);
        setStatus(Status.RUNNING);
    }

    /** 处理请求 */
    public void handle(HttpServletRequest request, HttpServletResponse response) {
        String result = resData.toJson();
        // 替换响应数据中的参数占位符
        if (isNotEmpty(query)) {
            Map<String, String> params = new HashMap<>();
            for (String k : query) {
                String v = request.getParameter(k);
                if (v != null) {
                    params.put(k, v);
                }
            }
            if (!params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String k = entry.getKey();
                    result = StringUtils.replace(result, "${" + k + "}", entry.getValue());
                }
            }
        }
        // 响应数据
        response.setContentType("application/json;charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(result);
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    protected void verify(Document params) {
        try {
            this.uri = NodeVerify.requiredUrl(params, "uri");
            this.method = HttpRequestNode.verifyMethod(params);

            String q = params.getString("query");
            if (StringUtils.hasLength(q)) {
                this.query = Arrays.asList(q.split(","));
            }

            String res = params.getString("resData");
            NodeVerify.notBlank(res, "resData");
            NodeVerify.isTrue(res.startsWith("{"), "resData");
            NodeVerify.isTrue(res.endsWith("}"), "resData");
            try {
                this.resData = Document.parse(res);
            } catch (Exception ignored) {
                NodeVerify.throwWithName("resData");
            }

            // 解析占位符
            List<String> keys = PlaceholderUtil.getKeys(resData);
            if (isNotEmpty(keys) && isNotEmpty(query)) {
                this.query = CollectionUtil.filter(keys, k -> query.contains(k));
            }
        } catch (Exception e) {
            throw FlowException.wrap(e, "The http node parameters is invalid");
        }
    }

    public String getMapping() {
        return uri + ":" + method;
    }

    @Override
    public void destroy() {
        HttpResponseServlet.removeHandlerMapping(this);
    }
}
