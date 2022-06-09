package com.flow.eda.runner.node.redis;

import org.bson.Document;
import org.junit.jupiter.api.Test;

class RedisNodeTest {

    @Test
    void run() {
        Document params = new Document();
        params.append("uri", "127.0.0.1:6379");
        params.append("method", "set");
        params.append("args", "test,Hello World!");
        // params.append("password", "123456");
        RedisNode node = new RedisNode(params);
        node.run(p -> System.out.println(p.toJson()));
    }
}
