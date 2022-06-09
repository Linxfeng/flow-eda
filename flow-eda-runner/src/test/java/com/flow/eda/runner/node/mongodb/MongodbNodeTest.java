package com.flow.eda.runner.node.mongodb;

import org.bson.Document;
import org.junit.jupiter.api.Test;

class MongodbNodeTest {

    @Test
    void run() {
        Document params = new Document();
        params.append("url", "mongodb://root:admin@127.0.0.1:27017/admin");
        params.append("db", "test_db");
        params.append("command", "{'find': 'test_coll', 'filter': {'name': 'test'}, 'limit': 1}");
        params.append(
                "command1",
                "{'update': 'test_coll', 'updates': [{'q':{},'u':{'$set':{'test': 'xxx'}}, 'multi': true}]}");
        MongodbNode node = new MongodbNode(params);
        node.run(p -> System.out.println(p.toJson()));
    }
}
