package com.flow.eda.runner.node.parser.xml;

import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class XmlParserNodeTest {

    @Test
    void run() {
        Document params = new Document();
        String xml =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<books>\n"
                        + "\t<book id=\"1\">\n"
                        + "\t\t<name class=\"xxa\">程序设计</name>\n"
                        + "\t\t<info>从入门到精通，带你玩转java编程</info>\n"
                        + "\t</book>\n"
                        + "\t<book id=\"2\">\n"
                        + "\t\t<name class=\"xxb\">架构设计</name>\n"
                        + "\t\t<info>从零开始，手把手教你设计大型网站架构</info>\n"
                        + "\t</book>\n"
                        + "</books>\n";

        params.append("httpResult", new Document("xml", xml));
        params.append("parseKey", "httpResult.xml");

        XmlParserNode node = new XmlParserNode(params);
        // node.run((p) -> System.out.println(p.toJson()));
        node.run(
                (p) ->
                        Assertions.assertTrue(
                                p.containsKey("result")
                                        && p.get("result", Document.class).containsKey("books")));
    }
}
