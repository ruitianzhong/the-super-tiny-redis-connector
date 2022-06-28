import org.junit.Assert;
import org.junit.Test;
import top.zhongruitian.Redis;

import java.util.UUID;

public class ConnectionTest {
    public static final int count = 10000;

    @Test
    public void checkPing() {
        Redis redis = new Redis();
        for (int i = 0; i < count; i++) {
            Assert.assertEquals("PONG", redis.ping());
        }
        Assert.assertEquals("OK", redis.quit());
    }


    @Test
    public void GetAndSetTest() {
        Redis redis = new Redis();

        for (int i = 0; i < count; i++) {
            String key = UUID.randomUUID().toString(), value = UUID.randomUUID().toString();
            redis.set(key, value);
            Assert.assertEquals(value, redis.get(key));
        }
        redis.close();
    }

    @Test
    public void flushAllTest() {
        Redis redis = new Redis();
        Assert.assertEquals("OK", redis.flushAll());
    }

    @Test
    public void GetSetPingTest() {
        Redis redis = new Redis();
        redis.flushAll();
        for (int i = 0; i < count; i++) {
            Assert.assertEquals("PONG", redis.ping());
            String key = UUID.randomUUID().toString(), value = UUID.randomUUID().toString();
            redis.set(key, value);
            Assert.assertEquals(value, redis.get(key));
        }
        for (int i = 0; i < count; i++) {
            Assert.assertEquals("PONG", redis.ping());
            String key = UUID.randomUUID().toString(), value = UUID.randomUUID().toString();
            redis.set(key, value);
            Assert.assertEquals(value, redis.get(key));
        }
        for (int i = 0; i < count; i++) {
            String key = UUID.randomUUID().toString(), value = UUID.randomUUID().toString();
            redis.set(key, value);
            Assert.assertEquals("PONG", redis.ping());
            Assert.assertEquals(value, redis.get(key));
        }
        for (int i = 0; i < count; i++) {
            String key = UUID.randomUUID().toString(), value = UUID.randomUUID().toString();
            redis.set(key, value);
            Assert.assertEquals(value, redis.get(key));
            Assert.assertEquals("PONG", redis.ping());
        }
        redis.quit();
    }

    @Test
    public void GetKeyIfNotExist() {
        Redis redis = new Redis();
        String s = redis.get("do not exist");
        Assert.assertNull(s);
        redis.quit();
    }
}
