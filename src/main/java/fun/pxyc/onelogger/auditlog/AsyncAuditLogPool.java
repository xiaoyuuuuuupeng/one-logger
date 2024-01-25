package fun.pxyc.onelogger.auditlog;

import fun.pxyc.onelogger.NamedThreadFactory;
import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncAuditLogPool {
    private static final Logger log = LoggerFactory.getLogger(AsyncAuditLogPool.class);
    private static AsyncAuditLogPool instance;
    private final int queueSize = 1000;
    private ThreadPoolExecutor asycAuditLogPool;

    public AsyncAuditLogPool() {}

    public static void asyncLog(Logger logger, String s) {
        if (instance != null) {
            instance.write(logger, s);
        }
    }

    public static void asyncLog(Logger logger, String s, Object... objects) {
        if (instance != null) {
            instance.write(logger, s, objects);
        }
    }

    public void init() {
        ThreadFactory asycAuditLogThreadFactory = new NamedThreadFactory("sq_envconfig_auditlog");
        this.asycAuditLogPool = new ThreadPoolExecutor(
                1, 1, 0L, TimeUnit.SECONDS, new ArrayBlockingQueue(this.queueSize), asycAuditLogThreadFactory);
        this.asycAuditLogPool.prestartAllCoreThreads();
        log.info("asyncAuditLogPool inited");
        instance = this;
    }

    public void close() {
        this.asycAuditLogPool.shutdown();
        log.info("asyncAuditLogPool closed");
        this.asycAuditLogPool = null;
        instance = null;
    }

    public void write(final Logger logger, final String s) {
        if (this.asycAuditLogPool != null) {
            try {
                this.asycAuditLogPool.execute(new Runnable() {
                    public void run() {
                        logger.info(s);
                    }
                });
            } catch (RejectedExecutionException e) {
                log.error("async audit asyncLog queue is full");
            }
        }
    }

    public void write(final Logger logger, final String s, Object... objects) {
        if (this.asycAuditLogPool != null) {
            try {
                this.asycAuditLogPool.execute(new Runnable() {
                    public void run() {
                        logger.info(s, objects);
                    }
                });
            } catch (RejectedExecutionException e) {
                log.error("async audit asyncLog queue is full");
            }
        }
    }
}
