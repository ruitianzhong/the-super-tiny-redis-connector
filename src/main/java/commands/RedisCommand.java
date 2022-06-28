package commands;

import util.SafeEncoder;

public enum RedisCommand implements Rawable {
    SET, GET, KEYS, MSET, MGET, PING, DEL, QUIT,FLUSHALL;

    @Override
    public byte[] getRaw() {
        return SafeEncoder.encode(this.name());
    }
}
