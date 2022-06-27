package commands;

import commands.Builder;
import util.SafeEncoder;

public class BuilderFactory {
    public static final Builder<String> SET = new Builder() {
        @Override
        public String build(Object data) {
            return ((byte[])data).toString();
        }
    };
    public static final Builder<String> GET = new Builder<String>() {
        @Override
        public String build(Object data) {
            return SafeEncoder.encode((byte[]) data);
        }
    };
}
