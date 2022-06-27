package util;

import java.net.Socket;

public interface RedisSocketFactory {
    Socket createSocket();
}
