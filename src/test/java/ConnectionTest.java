import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConnectionTest {
    @Test
    public void checkPing() throws InterruptedException {
        Connection connection = new Connection();
        connection.connect();
        int i = 100;
        while (i-- != 0) {
            String s = connection.ping();
            assertEquals(s, "PONG");
            Thread.sleep(300);
        }
        connection.disconnect();
    }
    @Test
    public void checkSet() throws InterruptedException {
        Connection connection = new Connection();
        connection.connect();
        System.out.println(connection.set("ruitian","012345678901234567890123456789"));
        System.out.println(connection.get("ruitian"));
        connection.disconnect();
    }

}
