package com.flow.eda.runner.node.parser.json;

import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import com.flow.eda.runner.utils.PlaceholderUtil;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

import static com.flow.eda.runner.node.NodeVerify.isTrue;
import static com.flow.eda.runner.node.NodeVerify.notNull;

/** JSON 解析器节点，用于解析上一个节点的输出参数 */
public class JsonParserNode extends AbstractNode {
    private List<String> keys;
    private Document payload;

    public JsonParserNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        Document result = PlaceholderUtil.parse(payload, keys);
        Document input = getInput();
        if (!input.isEmpty()) {
            result.append("params", input);
        }
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
            this.keys = Arrays.asList(parseKeys);
            isTrue(!keys.isEmpty());
            this.payload = new Document(params).append("params", getInput());
        } catch (Exception ignore) {
            NodeVerify.throwWithName("parseKey");
        }
    }
}
