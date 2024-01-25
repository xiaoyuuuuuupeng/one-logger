package fun.pxyc.onelogger.kafka;

import java.util.HashMap;

public class KafkaErrorCounter {
    private static final Object lock = new Object();
    private static HashMap<String, Integer> errorCount = new HashMap();

    public KafkaErrorCounter() {}

    public static void addErrorCount(String addr) {
        synchronized (lock) {
            int n = errorCount.getOrDefault(addr, 0);
            ++n;
            errorCount.put(addr, n);
        }
    }

    public static HashMap<String, Integer> getErrorCount() {
        synchronized (lock) {
            HashMap<String, Integer> t = errorCount;
            errorCount = new HashMap();
            return t;
        }
    }
}
