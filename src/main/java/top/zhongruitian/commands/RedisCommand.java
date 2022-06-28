package top.zhongruitian.commands;

import top.zhongruitian.util.SafeEncoder;

public enum RedisCommand implements Rawable {
    SET, GET, KEYS, MSET, MGET, PING, DEL, QUIT,FLUSHALL;

    @Override
    public byte[] getRaw() {
        return SafeEncoder.encode(this.name());
    }
}
