package com.flow.eda.runner.flow.node.parser;

import com.flow.eda.common.exception.InvalidParameterException;
import com.flow.eda.runner.flow.node.AbstractNode;
import com.flow.eda.runner.flow.node.NodeFunction;
import org.bson.Document;

import java.util.*;

import static com.flow.eda.runner.flow.node.NodeVerify.notNull;

/** 解析器节点，用于解析上一个节点的输出参数 */
public class ParserNode extends AbstractNode {
    private List<String[]> keys;
    private Document payload;

    public ParserNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        System.out.println("执行解析器节点");
        Document result = new Document();
        for (String[] k : keys) {
            Object temp = payload.get(k[0]);
            Object value = getValue(temp, k, 1);
            if (value != null) {
                result.append(k[k.length - 1], value);
            }
        }
        result.append("params", getInput());
        result.putAll(output());
        setStatus(Status.FINISHED);
        callback.callback(result);
    }

    @Override
    protected void verify(Document params) {
        try {
            notNull(params);
            // eg: httpResult.result.$0.type, params.a
            String parseKey = params.getString("parseKey");
            notNull(parseKey);
            String[] parseKeys = parseKey.split(",");
            this.keys = new ArrayList<>();
            Arrays.stream(parseKeys).map(key -> key.trim().split("\\.")).forEach(keys::add);
            this.payload = new Document(params).append("params", getInput());
        } catch (Exception ignore) {
            throw new InvalidParameterException("The parameter 'parseKey' is invalid");
        }
    }

    @SuppressWarnings("rawtypes")
    private Object getValue(Object value, String[] k, int i) {
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
