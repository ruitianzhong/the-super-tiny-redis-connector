package commands;

public class CommandObjects {
    public CommandObject<String> set(String key , String value)
    {
        return new CommandObject<>(BuilderFactory.SET,new CommandArguments(RedisCommand.SET).key(key).add(value));
    }
    public CommandObject<String> get(String key){
        return new CommandObject<>(BuilderFactory.GET,new CommandArguments(RedisCommand.GET).key(key));
    }

}
