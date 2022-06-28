package top.zhongruitian.exceptions;

/**
 * @author ruitianzhong
 * @email zhongruitian2003@qq.com
 */
public class RedisException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    public RedisException(String message) {
        super(message);
    }

    public RedisException(Throwable throwable) {
        super(throwable);
    }

    public RedisException(String message, Throwable cause) {
        super(message, cause);
    }

}
