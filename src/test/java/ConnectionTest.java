import commands.CommandObjects;
import org.junit.Test;

public class ConnectionTest {
    @Test
    public void checkPing() throws InterruptedException {
        Connection connection = new Connection();
        connection.connect();
        int i = 100;
        while (i-- != 0) {


            Thread.sleep(300);
        }
        connection.disconnect();
    }

    @Test
    public void checkSet() throws InterruptedException {
        Connection connection = new Connection();
        CommandObjects commandObjects = new CommandObjects();
        String s = connection.executeCommand(commandObjects.set("China", "zhongruitian"));
        System.out.println(s);
        String result = connection.executeCommand(commandObjects.get("China"));
        System.out.println(result);
    }

}
