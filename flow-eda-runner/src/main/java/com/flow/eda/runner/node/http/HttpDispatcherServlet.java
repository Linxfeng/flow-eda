package com.flow.eda.runner.node.http;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.http.receive.HttpReceiveNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/** 自定义DispatcherServlet */
@Slf4j
public class HttpDispatcherServlet extends DispatcherServlet {
    /** 用于存储mapping和对应的HttpReceiveNode节点处理器 */
    private static final Map<String, HttpReceiveNode> HANDLER_MAPPINGS = new HashMap<>();

    public static void addHandlerMapping(HttpReceiveNode node) {
        if (HANDLER_MAPPINGS.containsKey(node.getMapping())) {
            throw new FlowException("The same params 'uri' and 'method' node already exists");
        }
        HANDLER_MAPPINGS.put(node.getMapping(), node);
    }

    public static void removeHandlerMapping(HttpReceiveNode node) {
        HANDLER_MAPPINGS.remove(node.getMapping());
    }

    public static void responseData(String mapping, String data) {
        HttpReceiveNode httpReceiveNode = HANDLER_MAPPINGS.get(mapping);
        if (httpReceiveNode == null) {
            throw new FlowException("Missing http_receive node");
        }
        httpReceiveNode.responseData(data);
    }

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpReceiveNode handler = getHttpHandler(request);
        if (handler != null) {
            handler.handle(request, response);
        } else {
            super.doDispatch(request, response);
        }
    }

    private HttpReceiveNode getHttpHandler(HttpServletRequest request) {
        String mapping = request.getRequestURI() + ":" + request.getMethod();
        if (HANDLER_MAPPINGS.containsKey(mapping)) {
            return HANDLER_MAPPINGS.get(mapping);
        }
        return null;
    }
}
