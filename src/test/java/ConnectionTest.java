import org.junit.Test;

public class ConnectionTest {
    @Test
    public void checkConnection(){
        Connection connection=new Connection();
        connection.connect();
    }
}
