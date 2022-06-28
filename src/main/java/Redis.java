import commands.CommandObjects;

public class Redis {
    private Connection connection = null;
    private CommandObjects commandObjects;

    public Redis() {
        connection = new Connection();
        commandObjects = new CommandObjects();
    }

    public Redis(final String host, final int port) {
        connection = new Connection(host, port);
        commandObjects = new CommandObjects();
    }

    public String set(String key, String value) {
        return connection.executeCommand(commandObjects.set(key, value));
    }

    public String get(String key) {
        return connection.executeCommand(commandObjects.get(key));
    }

    public String ping() {
        return connection.executeCommand(commandObjects.ping());
    }
    public void  close(){
        connection.close();
    }
    public String quit(){
        return connection.quit();
    }
    public String flushAll(){
        return connection.executeCommand(commandObjects.flushAll());
    }
}
