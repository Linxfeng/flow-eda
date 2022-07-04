package com.flow.eda.runner.node;

import com.flow.eda.common.exception.InvalidParameterException;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

/** 节点参数校验工具 */
public class NodeVerify {

    public static void notNull(Object parameter) {
        notNull(parameter, "");
    }

    public static void notNull(Object parameter, String name) {
        if (parameter == null) {
            throwWithName(name);
        }
    }

    public static void notBlank(String parameter, String name) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throwWithName(name);
        }
    }

    public static void isTrue(Boolean condition) {
        isTrue(condition, "");
    }

    public static void isTrue(Boolean condition, String name) {
        if (!condition) {
            throwWithName(name);
        }
    }

    public static String requiredUrl(Document params, String url) {
        NodeVerify.notNull(params, url);
        String uri = params.getString(url);
        NodeVerify.notBlank(uri, url);
        NodeVerify.isTrue(uri.startsWith("/"), url);
        return uri;
    }

    public static String requiredMethod(Document params) {
        String method = params.getString("method");
        NodeVerify.notBlank(method, "method");
        List<String> list =
                Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "TRACE", "PATCH");
        NodeVerify.isTrue(list.contains(method), "method");
        return method;
    }

    public static void throwWithName(String name) {
        throw new InvalidParameterException("The parameter '" + name + "' is invalid");
    }
}
