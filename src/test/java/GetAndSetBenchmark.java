import org.junit.Test;
import top.zhongruitian.Redis;

public class GetAndSetBenchmark {
    public static final int count = 100000;

    @Test
    public void GetAndSetBenchmark() {
        Redis redis = new Redis();
        redis.flushAll();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            String key = "foo" + i, value = "bar" + i;
            redis.set(key, value);
            redis.get(key);
        }
        redis.quit();
        long end = System.currentTimeMillis();
        long elapsed = end - begin;
        System.out.println((2 * 1000 * count) / elapsed + " ops");
    }
}
