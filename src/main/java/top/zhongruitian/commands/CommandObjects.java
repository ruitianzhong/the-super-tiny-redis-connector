package top.zhongruitian.commands;

public class CommandObjects {
    public CommandObject<String> set(String key, String value) {
        return new CommandObject<>(BuilderFactory.SET, new CommandArguments(RedisCommand.SET).key(key).add(value));
    }

    public CommandObject<String> get(String key) {
        return new CommandObject<>(BuilderFactory.GET, new CommandArguments(RedisCommand.GET).key(key));
    }
    public CommandObject<String> ping(){
        return new CommandObject<>(BuilderFactory.PING,new CommandArguments(RedisCommand.PING));
    }

    public CommandObject<String> flushAll(){
        return new CommandObject<>(BuilderFactory.FLUSHALL,new CommandArguments(RedisCommand.FLUSHALL));
    }

}
