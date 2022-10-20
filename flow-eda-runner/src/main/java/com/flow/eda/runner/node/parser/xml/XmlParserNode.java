package com.flow.eda.runner.node.parser.xml;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import com.flow.eda.runner.utils.PlaceholderUtil;
import org.bson.Document;
import org.dom4j.Attribute;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** XML 解析器节点，用于解析xml格式的文本内容 */
public class XmlParserNode extends AbstractNode {
    private Element element;
    private String attrName = "@attributes";

    public XmlParserNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        // 解析xml
        Document res = new Document();
        this.parseXml2Doc(this.element, res);

        // 设置根节点对象
        if (!res.containsKey(this.element.getName())) {
            res = new Document(this.element.getName(), res);
        }

        setStatus(Status.FINISHED);
        callback.callback(output().append("result", res));
    }

    @Override
    protected void verify(Document params) {
        String parseKey = params.getString("parseKey");
        NodeVerify.notBlank(parseKey, "parseKey");
        if (parseKey.startsWith("params.")) {
            // 考虑取自定义参数的场景
            parseKey = parseKey.replace("params.", "input.");
        }

        // 根据parseKey获取xml
        Document result = PlaceholderUtil.parse(params, Collections.singletonList(parseKey));
        String xml = result.getString(parseKey);
        NodeVerify.notBlank(xml, "parseKey");

        // 校验xml格式，并解析取出根节点
        try {
            this.element = DocumentHelper.parseText(xml).getRootElement();
        } catch (Exception e) {
            throw new FlowException("Incorrect the xml format");
        }

        // 标签属性名称（默认为@attributes）
        String attr = params.getString("attr");
        if (StringUtils.hasText(attr)) {
            this.attrName = attr;
        }
    }

    /** 将xml的dom4j对象解析为Document对象 */
    @SuppressWarnings("unchecked")
    private void parseXml2Doc(Element element, Document res) {
        // 解析根节点属性
        this.parseAttr(element, res);

        // 解析子节点元素
        List<Element> childList = element.elements();
        if (childList.isEmpty()) {
            // 没有子元素时，直接设值返回
            res.append(element.getName(), element.getText());
            return;
        }

        for (Element childEl : childList) {
            // 子元素无子元素时，直接设值
            if (childEl.elements().isEmpty()) {
                this.parseAttr(childEl, res);
                if (StringUtils.hasText(childEl.getText())) {
                    res.append(childEl.getName(), childEl.getText());
                }
            } else {
                // 子元素有子元素时，需要递归处理
                Document childDoc = new Document();
                this.parseXml2Doc(childEl, childDoc);

                Object o = res.get(childEl.getName());
                // 若此子元素不存在，则存入对象
                if (o == null) {
                    if (!childDoc.isEmpty()) {
                        res.append(childEl.getName(), childDoc);
                    }
                } else {
                    // 若此子元素已存在时，需要转换为ArrayList数组
                    ArrayList<Document> list = new ArrayList<>();
                    if (o instanceof Document) {
                        list.add((Document) o);
                        list.add(childDoc);
                    } else if (o instanceof ArrayList) {
                        list = (ArrayList<Document>) o;
                        list.add(childDoc);
                    }
                    if (!list.isEmpty()) {
                        res.append(childEl.getName(), list);
                    }
                }
            }
        }
    }

    /** 解析节点的属性信息 */
    private void parseAttr(Element element, Document doc) {
        List<Attribute> attributes = element.attributes();
        if (!attributes.isEmpty()) {
            Document attrs = new Document();
            attributes.forEach(attr -> attrs.append(attr.getName(), attr.getValue()));
            doc.append(this.attrName, attrs);
        }
    }
}
