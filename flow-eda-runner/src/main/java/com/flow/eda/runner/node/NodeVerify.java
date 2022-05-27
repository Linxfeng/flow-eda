package com.flow.eda.runner.node;

import com.flow.eda.common.exception.InvalidParameterException;
import org.bson.Document;

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

    public static void throwWithName(String name) {
        throw new InvalidParameterException("The parameter '" + name + "' is invalid");
    }
}
