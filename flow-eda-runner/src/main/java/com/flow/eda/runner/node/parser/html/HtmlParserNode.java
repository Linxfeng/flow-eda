package com.flow.eda.runner.node.parser.html;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import com.flow.eda.runner.utils.PlaceholderUtil;
import org.bson.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/** HTML 解析器节点，用于解析html格式的文本内容 */
public class HtmlParserNode extends AbstractNode {
    private org.jsoup.nodes.Document document;
    private String filterTags;
    private String selectorValue;
    private Function<String, Elements> selectorFunction;
    private String outputType;

    public HtmlParserNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        // 解析目标元素
        Elements elements = selectorFunction.apply(selectorValue);

        // 二次过滤
        if (filterTags != null) {
            Elements newElements = new Elements();
            for (Element element : elements) {
                Elements elementsByTags = element.getElementsByTag(filterTags);
                newElements.addAll(elementsByTags);
            }
            elements = newElements;
        }

        // 输出指定格式
        List<String> result = new ArrayList<>();
        for (Element element : elements) {
            if ("html".equals(this.outputType)) {
                result.add(element.toString());
            } else {
                result.add(element.text());
            }
        }

        setStatus(Status.FINISHED);
        callback.callback(output().append("result", result));
    }

    @Override
    protected void verify(Document params) {
        String parseKey = params.getString("parseKey");
        NodeVerify.notBlank(parseKey, "parseKey");
        if (parseKey.startsWith("params.")) {
            // 考虑取自定义参数的场景
            parseKey = parseKey.replace("params.", "input.");
        }

        // 根据parseKey获取html
        Document result = PlaceholderUtil.parse(params, Collections.singletonList(parseKey));
        String html = result.getString(parseKey);
        NodeVerify.notBlank(html, "parseKey");

        try {
            this.document = Jsoup.parse(html);
        } catch (Exception e) {
            throw new FlowException("Incorrect the html format");
        }

        // 解析CSS选择器
        String selector = params.getString("selector");
        NodeVerify.notBlank(selector, "selector");
        NodeVerify.isTrue(selector.contains(","), "selector");

        String[] split = selector.split(",");
        this.selectorValue = split[0];
        this.parseSelector(split[1]);

        // 二次过滤标签
        String filterTags = params.getString("filterTags");
        if (StringUtils.hasText(filterTags)) {
            this.filterTags = filterTags;
        }

        // 默认输出文本内容
        String outputType = params.getString("outputType");
        if ("输出html内容".equals(outputType)) {
            this.outputType = "html";
        }
    }

    /** 解析CSS选择器 */
    private void parseSelector(String selector) {
        if ("tag".equals(selector)) {
            this.selectorFunction = (s) -> this.document.getElementsByTag(s);
        } else if ("class".equals(selector)) {
            this.selectorFunction = (s) -> this.document.getElementsByClass(s);
        } else if ("id".equals(selector)) {
            this.selectorFunction = (s) -> new Elements(this.document.getElementById(s));
        } else {
            NodeVerify.throwWithName("selector");
        }
    }
}
