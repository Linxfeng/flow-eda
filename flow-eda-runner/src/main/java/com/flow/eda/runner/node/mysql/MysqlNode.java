package com.flow.eda.runner.node.mysql;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import org.bson.Document;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MysqlNode extends AbstractNode {
    private String url;
    private String username;
    private String password;
    private String sql;
    private List<String> batchSql;

    public MysqlNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        Connection connection = null;
        try {
            // 加载驱动
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            // 建立数据库连接
            connection = DriverManager.getConnection(url, username, password);

            // 开启事务
            connection.setAutoCommit(false);
            Document result = new Document();

            // 执行sql语句
            if (batchSql != null) {
                for (int i = 0; i < batchSql.size(); i++) {
                    String s = batchSql.get(i);
                    Object resData = this.executeQuery(connection, s);
                    result.append("sql-" + (i + 1), s).append("result-" + (i + 1), resData);
                }
            } else {
                Object resData = this.executeQuery(connection, this.sql);
                result.append("sql", this.sql).append("result", resData);
            }
            // 提交事务
            connection.commit();

            // 输出结果
            setStatus(Status.FINISHED);
            result.putAll(output());
            callback.callback(result);
        } catch (Exception e) {
            // 发生异常后回滚
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ignored) {
                }
            }
            throw new FlowException(e.getMessage());
        } finally {
            // 关闭连接
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    /** 执行sql查询语句 */
    private Object executeQuery(Connection connection, String sql) throws SQLException {
        if (!sql.toLowerCase().startsWith("select")) {
            return this.executeUpdate(connection, sql);
        }
        List<Document> list = new ArrayList<>();
        // 执行sql语句
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        // 解析结果
        while (resultSet.next()) {
            Document result = new Document();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = resultSet.getObject(i);
                if (value != null && !(value instanceof String)) {
                    result.append(columnName, value.toString());
                } else {
                    result.append(columnName, value);
                }
            }
            list.add(result);
        }

        // 关闭statement
        resultSet.close();
        statement.close();

        // 返回结果
        Object resData;
        if (list.isEmpty()) {
            resData = "{}";
        } else if (list.size() == 1) {
            resData = list.get(0);
        } else {
            resData = list;
        }
        return resData;
    }

    /** 执行sql更新/插入/删除等语句 */
    private String executeUpdate(Connection connection, String sql) throws SQLException {
        // 执行sql语句
        PreparedStatement statement = connection.prepareStatement(sql);
        int update = statement.executeUpdate();
        statement.close();
        return "Affected rows: " + update;
    }

    @Override
    protected void verify(Document params) {
        this.url = params.getString("url");
        NodeVerify.notBlank(url, "url");

        // 关闭SSL，缩短建立连接所耗费的时间
        if (!url.contains("useSSL")) {
            if (url.contains("?")) {
                this.url = url + "&useSSL=false";
            } else {
                this.url = url + "?useSSL=false";
            }
        }

        this.username = params.getString("username");
        NodeVerify.notBlank(username, "username");

        this.password = params.getString("password");
        NodeVerify.notBlank(password, "password");

        String sql = params.getString("sql");
        NodeVerify.notBlank(sql, "sql");

        // 去除末尾分号
        String semicolon = ";";
        if (sql.endsWith(semicolon)) {
            sql = sql.substring(0, sql.length() - 1);
        }

        // 处理多条sql语句
        if (sql.contains(semicolon)) {
            this.batchSql = Arrays.asList(sql.split(semicolon));
        } else {
            this.sql = sql;
        }
    }
}
