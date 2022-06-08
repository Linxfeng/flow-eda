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
        // 建立jedis连接
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
            // 调用jedis方法
            Object result = this.invoke(jedis);

            setStatus(Status.FINISHED);
            callback.callback(output().append("result", result));
        } catch (Exception e) {
            throw new FlowException(e.getMessage());
        }
    }

    /** 调用jedis对应的方法 */
    private Object invoke(Jedis jedis) {
        this.checkArgsLength(1);
        /* set,get,del,exists,getSet,getDel,hset,hget,hdel,hgetAll,hexists */
        switch (method) {
            case "set":
                this.checkArgsLength(2);
                return jedis.set(args[0], args[1]);
            case "get":
                return jedis.get(args[0]);
            case "del":
                return jedis.del(args);
            case "exists":
                return jedis.exists(args);
            case "getSet":
                this.checkArgsLength(2);
                return jedis.getSet(args[0], args[1]);
            case "getDel":
                return jedis.getDel(args[0]);
            case "hset":
                this.checkArgsLength(3);
                return jedis.hsetnx(args[0], args[1], args[2]);
            case "hget":
                this.checkArgsLength(2);
                return jedis.hget(args[0], args[1]);
            case "hdel":
                this.checkArgsLength(2);
                String[] fields = new String[args.length - 1];
                System.arraycopy(args, 1, fields, 0, args.length - 1);
                return jedis.hdel(args[0], fields);
            case "hgetAll":
                return jedis.hgetAll(args[0]);
            case "hexists":
                this.checkArgsLength(2);
                return jedis.hexists(args[0], args[1]);
            default:
                throw new FlowException("Method '" + method + "' is not supported yet");
        }
    }

    /** 校验参数的长度 */
    private void checkArgsLength(int length) {
        if (args.length < length) {
            throw new FlowException("The args for this method is invalid");
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
