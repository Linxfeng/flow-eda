package com.flow.eda.runner.node.parser.html;

import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HtmlParserNodeTest {

    @Test
    void run() {
        Document params = new Document();
        String html =
                "<!DOCTYPE html>\n"
                        + "<html lang=\"en\">\n"
                        + "  \n"
                        + "  <head>\n"
                        + "    <meta charset=\"UTF-8\">\n"
                        + "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                        + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                        + "    <title>Document</title>\n"
                        + "    <style>h1 , h5{ text-align: center; } div { text-align: center; }</style>\n"
                        + "  </head>\n"
                        + "  \n"
                        + "  <body>\n"
                        + "    <h1>\n"
                        + "      <p id=\"p1\">《青溪》</p>\n"
                        + "    </h1>\n"
                        + "    <h5>\n"
                        + "      <p id=\"p2\">作者：王维</p>\n"
                        + "    </h5>\n"
                        + "    <div>\n"
                        + "      <p class=\"p3\">言入黄花川，每逐青溪水。</p>\n"
                        + "      <p class=\"p3\">随山将万转，趣途无百里。</p>\n"
                        + "      <p class=\"p3\">声喧乱石中，色静深松里。</p>\n"
                        + "      <p class=\"p3\">漾漾泛菱荇，澄澄映葭苇。</p>\n"
                        + "      <p class=\"p3\">我心素已闲，清川澹如此。</p>\n"
                        + "      <p class=\"p3\">请留盘石上，垂钓将已矣。</p>\n"
                        + "    </div>\n"
                        + "  </body>\n"
                        + "\n"
                        + "</html>";
        params.append("httpResult", new Document("result", html));
        params.append("parseKey", "httpResult.result");
        // params.append("selector", "p3,class");
        params.append("selector", "p,tag");
        // params.append("filterTags", "p");
        // params.append("outputType", "输出html内容");

        HtmlParserNode node = new HtmlParserNode(params);
        // node.run((p) -> System.out.println(p.toJson()));
        node.run(
                (p) ->
                        Assertions.assertTrue(
                                p.containsKey("result")
                                        && p.getList("result", String.class).size() == 8));
    }
}
