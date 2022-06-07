package com.flow.eda.runner.node.mongodb;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

public class MongodbNode extends AbstractNode {
    private String url;
    private String db;
    private String command;

    public MongodbNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        try (MongoClient mongoClient = MongoClients.create(url)) {
            MongoDatabase database = mongoClient.getDatabase(db);

            FindIterable<Document> testColl =
                    database.getCollection("test_coll").find(Filters.eq("name", "test"));
            for (Document value : testColl) {
                System.out.println(value.toJson());
            }

            String c = "{\"find\": \"test_coll\", \"filter\": {\"name\": \"test\"}}";
            Document document = database.runCommand(Document.parse(c));

            Document cursor = document.get("cursor", Document.class);
            List<Document> firstBatch = cursor.getList("firstBatch", Document.class);
            System.out.println(Arrays.toString(firstBatch.toArray()));

        } catch (Exception e) {
            e.printStackTrace();
            throw new FlowException(e.getMessage());
        }
    }

    @Override
    protected void verify(Document params) {
        this.url = params.getString("url");
        this.db = params.getString("db");
        this.command = params.getString("command");
    }
}
