package top.zhongruitian.exceptions;

public class RedisConnectionException extends RedisException {
    private static final long serialVersionUID = 1L;

    public RedisConnectionException(String message) {
        super(message);
    }

    public RedisConnectionException(Throwable cause) {
        super(cause);

    }

    public RedisConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
