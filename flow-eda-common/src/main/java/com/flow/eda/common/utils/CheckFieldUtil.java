package com.flow.eda.common.utils;

import com.flow.eda.common.exception.MissingPropertyException;

/** 字段校验工具 */
public class CheckFieldUtil {

    public static void missingProperty(String name, Object value) {
        if (value == null) {
            throw new MissingPropertyException(name);
        }
    }
}
