package com.flow.eda.runner.node.mongodb;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongodbNode extends AbstractNode {
    private String url;
    private String db;
    private Document command;

    public MongodbNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        try (MongoClient mongoClient = MongoClients.create(url)) {
            MongoDatabase database = mongoClient.getDatabase(db);
            Document document = database.runCommand(command);

            setStatus(Status.FINISHED);
            callback.callback(output().append("result", document));
        } catch (Exception e) {
            throw new FlowException(e.getMessage());
        }
    }

    @Override
    protected void verify(Document params) {
        this.url = params.getString("url");
        NodeVerify.notBlank(url, "url");

        this.db = params.getString("db");
        NodeVerify.notBlank(db, "db");

        try {
            String cm = params.getString("command");
            NodeVerify.notNull(cm);
            this.command = Document.parse(cm);
            NodeVerify.isTrue(!command.isEmpty());
        } catch (Exception e) {
            NodeVerify.throwWithName("command");
        }
    }
}
