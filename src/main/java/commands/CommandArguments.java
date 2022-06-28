package commands;

import java.util.ArrayList;
import java.util.Iterator;

public class CommandArguments implements Iterable<Rawable> {
    private final ArrayList<Rawable> args;

    public CommandArguments(RedisCommand redisCommand) {
        args = new ArrayList<>();
        args.add(redisCommand);
    }

    public CommandArguments key(Object key) {

        if (key instanceof Rawable) {
            args.add((Rawable) key);
        } else if (key instanceof String) {
            args.add(new StringRaw((String) key));
        } else if (key instanceof Boolean) {
            args.add(new StringRaw(Integer.toString((Boolean) key ? 1 : 0)));
        } else if (key == null) {
            throw new IllegalArgumentException("\"" + key.toString() + "\" is not a valid argument");
        }
        return this;
    }

    public CommandArguments add(Object o) {


        if (o instanceof Rawable) {
            args.add((Rawable) o);
        } else if (o instanceof String) {
            args.add(new StringRaw((String) o));
        } else if (o instanceof Boolean) {
            args.add(new StringRaw(Integer.toString((Boolean) o ? 1 : 0)));
        } else if (o instanceof byte[]) {
            args.add(new StringRaw((byte[]) o));
        } else if (o == null) {
            throw new IllegalArgumentException("null is not valid argument");
        } else {
            args.add(new StringRaw(String.valueOf(o)));
        }
        return this;
    }

    public int size() {
        return args.size();
    }

    @Override
    public Iterator<Rawable> iterator() {
        return args.iterator();
    }
}
