package top.zhongruitian.commands;

public class CommandObject<T> {
    private Builder<T> builder;
    private CommandArguments args;

    public CommandObject(Builder builder, CommandArguments args) {
        this.builder = builder;
        this.args = args;
    }

    public Builder<T> getBuilder() {
        return this.builder;
    }

    public CommandArguments getArgs() {
        return this.args;
    }


}
