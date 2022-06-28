package top.zhongruitian.commands;

import top.zhongruitian.util.SafeEncoder;

public class StringRaw extends Raw {

    public StringRaw(String str) {
        super(SafeEncoder.encode(str));
    }

    public StringRaw(byte[] bytes) {
        super(bytes);
    }
}
