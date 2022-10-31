package com.flow.eda.runner.node.postgresql;

import org.bson.Document;
import org.junit.jupiter.api.Test;

class PostgresqlNodeTest {

    @Test
    void run() {
        Document params = new Document();
        params.append("url", "jdbc:postgresql://localhost:5432/postgres");
        params.append("username", "postgres");
        params.append("password", "123456");
        params.append("sql", "SELECT * FROM test WHERE id=1");
        PostgresqlNode node = new PostgresqlNode(params);
        node.run(p -> System.out.println(p.toJson()));
    }
}
