package util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SafeEncoder {
    public static volatile Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static String encode(final byte[] data) {
    if (data ==null){
        return null;
    }
        return new String(data, DEFAULT_CHARSET);
    }

    public static byte[] encode(String str) {
        if (str == null) {
            throw new IllegalArgumentException("null value can not be sent to redis");
        }
        return str.getBytes(DEFAULT_CHARSET);
    }
}
