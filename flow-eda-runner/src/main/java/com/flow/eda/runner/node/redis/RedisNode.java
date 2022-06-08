package com.flow.eda.runner.node.redis;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import org.bson.Document;
import redis.clients.jedis.Jedis;

public class RedisNode extends AbstractNode {
    private String host;
    private Integer port;
    private String method;
    private String[] args;
    private String username;
    private String password;
    private Integer database;

    public RedisNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        try (Jedis jedis = new Jedis(host, port)) {

            // 设置auth认证
            if (password != null) {
                if (username != null) {
                    jedis.auth(username, password);
                } else {
                    jedis.auth(password);
                }
            }
            // 切换数据库
            if (database != null) {
                jedis.select(database);
            }

            String test = jedis.set("test", "hello world!");
            System.out.println(test);

            String result = jedis.get("test");
            System.out.println(result);

            Long del = jedis.del("test");
            System.out.println(del);

        } catch (Exception e) {
            throw new FlowException(e.getMessage());
        }
    }

    @Override
    protected void verify(Document params) {
        try {
            String uri = params.getString("uri");
            NodeVerify.notNull(uri);
            NodeVerify.isTrue(uri.contains(":"));

            this.host = uri.split(":")[0];
            NodeVerify.notNull(host);

            String port = uri.split(":")[1];
            NodeVerify.notNull(port);
            this.port = Integer.parseInt(port);
        } catch (Exception e) {
            NodeVerify.throwWithName("uri");
        }

        this.method = params.getString("method");
        NodeVerify.notNull(method, "method");

        String args = params.getString("args");
        NodeVerify.notNull(args, "args");

        if (args.contains(",")) {
            this.args = args.split(",");
        } else {
            this.args = new String[1];
            this.args[0] = args;
        }

        this.username = params.getString("username");
        this.password = params.getString("password");

        String db = params.getString("database");
        if (db != null) {
            try {
                this.database = Integer.parseInt(db);
            } catch (Exception e) {
                NodeVerify.throwWithName("database");
            }
        }
    }
}
