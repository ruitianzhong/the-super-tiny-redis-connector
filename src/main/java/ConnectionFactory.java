import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import util.RedisSocketFactory;

public class ConnectionFactory implements PooledObjectFactory<Connection> {
    private RedisSocketFactory redisSocketFactory;
    private String host = Protocol.DEFAULT_HOST;
    private int port = Protocol.DEFAULT_PORT;

    public ConnectionFactory(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public ConnectionFactory() {

    }

    @Override
    public void activateObject(PooledObject<Connection> p) throws Exception {

    }

    @Override
    public void destroyObject(PooledObject<Connection> p) throws Exception {
        final Connection connection = p.getObject();
        if (connection.isConnected()) {
            if (!connection.isBroken()) {
                connection.quit();
            }
        }
        connection.close();
    }

    @Override
    public PooledObject<Connection> makeObject() throws Exception {
        Connection connection = null;
        connection = new Connection(host, port);
        connection.connect();
        return new DefaultPooledObject<>(connection);
    }

    @Override
    public void passivateObject(PooledObject<Connection> p) throws Exception {

    }

    @Override
    public boolean validateObject(PooledObject<Connection> p) {
        Connection connection = p.getObject();
        return connection.isConnected();
    }
}
