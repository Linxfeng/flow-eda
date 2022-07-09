package com.flow.eda.runner.utils;

import com.flow.eda.common.utils.CollectionUtil;
import org.bson.Document;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** 解析占位符${}的工具类 */
public class PlaceholderUtil {
    private static final Pattern REGEX = Pattern.compile("\\$\\{([^}]*)}");

    /**
     * 替换占位符
     *
     * @param output 输出参数，含有占位符
     * @param input 输入参数，占位符对应的值从此处取
     * @return 输出替换值后的参数
     */
    public static Document replacePlaceholder(Document output, Document input) {
        List<String> keys = getKeys(output);
        if (CollectionUtil.isNotEmpty(keys)) {
            return PlaceholderUtil.fillValue(output, keys, parse(input, keys));
        }
        return output;
    }

    /**
     * 解析占位符
     *
     * @param input 入参，占位符key对应的值从此处取
     * @param keys 占位符中的key值.(${key}中的key，不可包含$等符号)
     * @return 返回一个Document，内含所有解析到的key-value值
     */
    public static Document parse(Document input, List<String> keys) {
        Document result = new Document();
        for (String key : keys) {
            if (input.containsKey(key)) {
                result.append(key, input.get(key));
            } else {
                String[] k = key.split("\\.");
                Object temp = input.get(k[0]);
                Object value = getValue(temp, k, 1);
                if (value != null) {
                    result.append(key, value);
                }
            }
        }
        return result;
    }

    /**
     * 获取含有占位符的所有key值
     *
     * @param p 需要获取占位符的参数
     * @return 返回占位符字段keys
     */
    public static List<String> getKeys(Document p) {
        if (p == null) {
            return null;
        }
        String str = p.toJson();
        if (!str.contains("${")) {
            return null;
        }
        List<String> list = new ArrayList<>();
        Matcher matcher = REGEX.matcher(str);
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        return list.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 在占位符的位置上填充值
     *
     * @param output 含有占位符的参数
     * @param keys 将要填充值的keys
     * @param res 占位符keys所对应的value值
     * @return 返回填充值后的结果
     */
    public static Document fillValue(Document output, List<String> keys, Document res) {
        String str = output.toJson();
        for (String k : keys) {
            Object value = res.get(k);
            if (value != null) {
                str = StringUtils.replace(str, "${" + k + "}", value.toString());
            }
        }
        try {
            return Document.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    @SuppressWarnings("rawtypes")
    private static Object getValue(Object value, String[] k, int i) {
        if (i == k.length || value == null) {
            return value;
        }
        Object temp = null;
        String key = k[i];
        if (value instanceof Document || value instanceof LinkedHashMap) {
            temp = ((Map<?, ?>) value).get(key);
        } else if (value instanceof ArrayList) {
            if (key.startsWith("$")) {
                String s = key.substring(1);
                int index = Integer.parseInt(s);
                temp = ((ArrayList) value).get(index);
            }
        }
        return getValue(temp, k, ++i);
    }
}
