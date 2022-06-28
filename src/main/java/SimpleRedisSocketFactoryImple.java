import exceptions.RedisConnectionException;
import util.RedisSocketFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimpleRedisSocketFactoryImple implements RedisSocketFactory {
    private String host = Protocol.DEFAULT_HOST;
    private int port = Protocol.DEFAULT_PORT;

    private int socketTimeOut = Protocol.DEFAULT_TIME_OUT;

    public SimpleRedisSocketFactoryImple() {
    }

    public SimpleRedisSocketFactoryImple(String host, int port) {
        this.host = host;
        this.port = port;
    }


    @Override
    public Socket createSocket() {
        Socket socket = null;
        try {
            socket = connectToFirstSuccessfulHost(this.host, this.port);
            socket.setSoTimeout(socketTimeOut);
            return socket;
        } catch (Exception ex) {
            closeSocketQuietly(socket);
            if (ex instanceof RedisConnectionException) {
                throw (RedisConnectionException) ex;
            } else {
                throw new RedisConnectionException(ex);
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

    private Socket connectToFirstSuccessfulHost(String host, int port) throws Exception {
        List<InetAddress> hosts = Arrays.asList(InetAddress.getAllByName(host));
        if (hosts.size() > 1) {
            Collections.shuffle(hosts);
        }
        RedisConnectionException rce = new RedisConnectionException("Failed to connect to any host resolved for DNS name.");
        for (InetAddress address : hosts) {
            try {
                Socket socket = new Socket();
                socket.setReuseAddress(true);
                socket.setKeepAlive(true);
                socket.setTcpNoDelay(true);
                socket.setSoLinger(true, 0);
                socket.connect(new InetSocketAddress(host, port));
                return socket;
            } catch (Exception e) {
                rce.addSuppressed(e);
            }

        }

        throw rce;
    }
}
