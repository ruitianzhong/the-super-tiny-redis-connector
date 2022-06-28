package commands;

import util.SafeEncoder;

import static util.SafeEncoder.encode;

public class BuilderFactory {
    public static final Builder<String> SET = new Builder() {
        @Override
        public String build(Object data) {
            return encode((byte[]) data);
        }
    };
    public static final Builder<String> GET = new Builder<String>() {
        @Override
        public String build(Object data) {
            return encode((byte[]) data);
        }
    };
    public static final Builder<String> PING = new Builder<String>() {
        @Override
        public String build(Object data) {
            return SafeEncoder.encode((byte[]) data);
        }
    };
    public static final Builder<String> FLUSHALL = new Builder<String>() {
        @Override
        public String build(Object data) {
            return SafeEncoder.encode((byte[]) data);
        }
    };
}
