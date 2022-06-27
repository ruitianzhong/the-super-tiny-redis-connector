import exceptions.RedisConnectionException;
import util.RedisConfig;
import util.RedisInputStream;
import util.RedisOutputStream;
import util.RedisSocketFactory;

import java.io.IOException;
import java.net.Socket;

public class Connection {
    private RedisOutputStream redisOutputStream = null;
    private RedisInputStream redisInputStream = null;
    private Socket socket = null;
    private RedisConfig redisConfig;
    private ConnectionPool memberof = null;
    private boolean isBroken = false;
    private RedisSocketFactory redisSocketFactory;
    private int soTimeOut = 0;

    public Connection() {
        this(Protocol.DEFAULT_HOST, Protocol.DEFAULT_PORT);
    }

    public Connection(String host, int port) {
        this(host, port, null);
    }

    public Connection(String host, int port, RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
        this.redisSocketFactory = new SimpleRedisSocketFactoryImple(host, port);
    }

    public void connect() {
        if (!isConnected()) {
            try {
                this.socket = redisSocketFactory.createSocket();
                this.soTimeOut = socket.getSoTimeout();
                this.redisInputStream = new RedisInputStream(socket.getInputStream());
                this.redisOutputStream = new RedisOutputStream(socket.getOutputStream());

            } catch (IOException ioe) {
                isBroken = true;
                throw new RedisConnectionException("Fail to create output or input stream");
            } catch (RedisConnectionException rce) {
                isBroken = true;
            } finally {
                if (isBroken) {
                    closeSocketQuietly(socket);
                }
            }
        }
    }

    public void closeSocketQuietly(Socket socket) {
        if(socket!=null)
        { try {
            socket.close();
        } catch (IOException ioe) {
            //do nothing
        }}
    }

    public boolean isConnected() {
        return socket != null && socket.isBound() && !socket.isClosed() && socket.isConnected()
                && !socket.isInputShutdown() && !socket.isOutputShutdown();
    }
    public void close(){
       //TODO : support the connection pool
    }

    public void disconnect(){
        if(isConnected()){
            try {
                redisOutputStream.flush();
                socket.close();
            }catch (IOException ioe){
                isBroken=true;
                throw new RedisConnectionException(ioe);
            }finally {
                closeSocketQuietly(socket);
            }
        }
    }
}
