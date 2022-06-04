package com.flow.eda.runner.node.mysql;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import org.bson.Document;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlNode extends AbstractNode {
    private String url;
    private String username;
    private String password;
    private String sql;

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
            // 执行sql语句
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            List<Document> list = new ArrayList<>();
            while (resultSet.next()) {
                Document result = new Document();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    result.append(columnName, value);
                }
                list.add(result);
            }
            // 自动提交，开启事务？

            // 关闭statement
            resultSet.close();
            statement.close();

            // 输出结果
            Object resData = null;
            if (list.size() == 1) {
                resData = list.get(0);
            } else if (list.size() > 1) {
                resData = list;
            }
            Document output = output();
            if (resData != null) {
                output.append("result", resData);
            }
            callback.callback(output);
        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    protected void verify(Document params) {
        this.url = params.getString("url");
        this.username = params.getString("username");
        this.password = params.getString("password");
        this.sql = params.getString("sql");
    }
}
