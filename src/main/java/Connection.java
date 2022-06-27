import exceptions.RedisConnectionException;
import util.RedisConfig;
import util.RedisInputStream;
import util.RedisOutputStream;
import util.RedisSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ioe) {
                //do nothing
            }
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isBound() && !socket.isClosed() && socket.isConnected()
                && !socket.isInputShutdown() && !socket.isOutputShutdown();
    }

    public void close() {
        //TODO : support the connection pool
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                redisOutputStream.flush();
                socket.close();
            } catch (IOException ioe) {
                isBroken = true;
                throw new RedisConnectionException(ioe);
            } finally {
                closeSocketQuietly(socket);
            }
        }
    }

    public String ping() {
        try {
            redisOutputStream.write('*');
            redisOutputStream.writeIntCrLf(1);
            redisOutputStream.write((byte) '$');
            redisOutputStream.writeIntCrLf(4);
            redisOutputStream.write("PING".getBytes(StandardCharsets.UTF_8));
            redisOutputStream.writeCrLf();

        } catch (IOException ioe) {
            throw new RedisConnectionException(ioe);

        }
        try {
            redisOutputStream.flush();
            byte b = redisInputStream.readByte();
            byte[] store = redisInputStream.readLineBytes();
            return new String(store, StandardCharsets.UTF_8);

        } catch (IOException ioe) {
            isBroken = true;
            throw new RedisConnectionException(ioe);
        }

    }

    public String set(String key, String value) {

        try {
            redisOutputStream.write('*');
            redisOutputStream.writeIntCrLf(3);
            redisOutputStream.write((byte) '$');
            redisOutputStream.writeIntCrLf(3);
            redisOutputStream.write("SET".getBytes(StandardCharsets.UTF_8));
            redisOutputStream.writeCrLf();
            redisOutputStream.write((byte) '$');
            redisOutputStream.writeIntCrLf(key.length());
            redisOutputStream.write(key.getBytes(StandardCharsets.UTF_8));
            redisOutputStream.writeCrLf();
            redisOutputStream.write((byte) '$');
            redisOutputStream.writeIntCrLf(value.length());
            redisOutputStream.write(value.getBytes(StandardCharsets.UTF_8));
            redisOutputStream.writeCrLf();
        } catch (IOException ioe) {
            throw new RedisConnectionException(ioe);

        }
        try {
            redisOutputStream.flush();
            byte b = redisInputStream.readByte();
            byte[] store = redisInputStream.readLineBytes();
            return new String(store, StandardCharsets.UTF_8);

        } catch (IOException ioe) {
            isBroken = true;
            throw new RedisConnectionException(ioe);
        }

    }

    public String get(String key) {
        try {
            redisOutputStream.write('*');
            redisOutputStream.writeIntCrLf(2);
            redisOutputStream.write((byte) '$');
            redisOutputStream.writeIntCrLf(3);
            redisOutputStream.write("GET".getBytes(StandardCharsets.UTF_8));
            redisOutputStream.writeCrLf();
            redisOutputStream.write((byte) '$');
            redisOutputStream.writeIntCrLf(key.length());
            redisOutputStream.write(key.getBytes(StandardCharsets.UTF_8));
            redisOutputStream.writeCrLf();

        } catch (IOException ioe) {
            throw new RedisConnectionException(ioe);

        }
        try {
            redisOutputStream.flush();
            System.out.println((char) redisInputStream.readByte());

            final int len = redisInputStream.readIntCrLf();
            System.out.println(len);
            if (len == -1) {
                return null;
            }
            byte[] read = new byte[len];

            int offset = 0;
            while (offset < len) {
                final int size = redisInputStream.read(read, offset, (len - offset));
                //write more than one line??
                offset += size;
            }
            redisInputStream.readByte();
            redisInputStream.readByte();

            return new String(read, StandardCharsets.UTF_8);

        } catch (IOException ioe) {
            isBroken = true;
            throw new RedisConnectionException(ioe);
        }

    }


}
