package com.flow.eda.runner.node.mongodb;

import org.bson.Document;
import org.junit.jupiter.api.Test;

class MongodbNodeTest {

    @Test
    void run() {
        Document params = new Document();
        params.append("url", "mongodb://admin:admin@127.0.0.1:27017/admin");
        params.append("db", "test_db");
        params.append("command", "db.getCollection('test_coll').find({\"name\" : \"test\"})");
        MongodbNode node = new MongodbNode(params);
        node.run(p -> System.out.println(p.toJson()));
    }
}
