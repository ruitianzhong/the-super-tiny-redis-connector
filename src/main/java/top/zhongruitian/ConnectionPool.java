package top.zhongruitian;

import top.zhongruitian.util.Pool;

public class ConnectionPool extends Pool<Connection> {
    public ConnectionPool() {
        super(new ConnectionFactory());
    }

    public ConnectionPool(String host, int port) {
        super(new ConnectionFactory(host, port));
    }

    public Connection getResource() {
        Connection connection = super.getResource();
        connection.setMemberof(this);
        return connection;
    }


}
