package com.flow.eda.runner.node.http.response;

import com.flow.eda.common.exception.FlowException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/** 自定义DispatcherServlet */
@Slf4j
public class HttpResponseServlet extends DispatcherServlet {
    /** 用于存储mapping和对应的HttpResponseNode节点处理器 */
    private static final Map<String, HttpResponseNode> HANDLER_MAPPINGS = new HashMap<>();

    public static void addHandlerMapping(HttpResponseNode node) {
        if (HANDLER_MAPPINGS.containsKey(node.getMapping())) {
            throw new FlowException("The same params 'uri' and 'method' node already exists");
        }
        HANDLER_MAPPINGS.put(node.getMapping(), node);
    }

    public static void removeHandlerMapping(HttpResponseNode node) {
        HANDLER_MAPPINGS.remove(node.getMapping());
    }

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpResponseNode handler = getHttpResponseHandler(request);
        if (handler != null) {
            handler.handle(request, response);
        } else {
            super.doDispatch(request, response);
        }
    }

    private HttpResponseNode getHttpResponseHandler(HttpServletRequest request) {
        String mapping = request.getRequestURI() + ":" + request.getMethod();
        if (HANDLER_MAPPINGS.containsKey(mapping)) {
            return HANDLER_MAPPINGS.get(mapping);
        }
        return null;
    }
}
