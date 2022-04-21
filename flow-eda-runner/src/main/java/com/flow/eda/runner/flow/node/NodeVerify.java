package com.flow.eda.runner.flow.node;

import com.flow.eda.common.exception.InvalidParameterException;

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

    public static void throwWithName(String name) {
        throw new InvalidParameterException("The parameter '" + name + "' is invalid");
    }
}
