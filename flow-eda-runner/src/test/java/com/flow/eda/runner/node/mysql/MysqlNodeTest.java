package com.flow.eda.runner.node.mysql;

import org.bson.Document;
import org.junit.jupiter.api.Test;

class MysqlNodeTest {

    @Test
    void run() {
        Document params = new Document();
        params.append(
                "url",
                "jdbc:mysql://localhost:3306/flow_eda?useSSL=false&useUnicode=true&characterEncoding=utf-8");
        params.append("username", "root");
        params.append("password", "123456");
        params.append("sql", "SELECT id,type,type_name FROM eda_flow_node_type WHERE id IN (1,2)");
        MysqlNode node = new MysqlNode(params);
        node.run(p -> System.out.println(p.toJson()));
    }
}
