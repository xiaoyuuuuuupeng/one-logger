package fun.pxyc.onelogger;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
    private final String namePrefix;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final SecurityManager s = System.getSecurityManager();
    private final ThreadGroup group;

    public NamedThreadFactory(String namePrefix) {
        this.group = this.s != null
                ? this.s.getThreadGroup()
                : Thread.currentThread().getThreadGroup();
        this.namePrefix = namePrefix;
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(this.group, r, this.namePrefix + "-thread-" + this.threadNumber.getAndIncrement(), 0L);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }

        if (t.getPriority() != 5) {
            t.setPriority(5);
        }

        return t;
    }
}
