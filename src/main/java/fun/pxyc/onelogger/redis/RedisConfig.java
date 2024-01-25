package fun.pxyc.onelogger.redis;

import java.util.concurrent.atomic.AtomicInteger;

public class RedisConfig {

    public static final String LOG_TYPE = "REDIS.";
    public static AtomicInteger seq = new AtomicInteger(0);

    public RedisConfig() {}
}
