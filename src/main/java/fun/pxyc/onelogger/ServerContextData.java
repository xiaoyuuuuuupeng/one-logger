package fun.pxyc.onelogger;

import fun.pxyc.onelogger.trace.ContextData;
import fun.pxyc.onelogger.trace.MetaData;
import fun.pxyc.onelogger.trace.TraceContext;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerContextData extends ContextData {
    private final TraceContext traceContext;
    private final AtomicBoolean replied = new AtomicBoolean(false);
    private long decodeMicros = 0L;
    private long waitInQueueMicros = 0L;

    public ServerContextData(String connId, MetaData meta, TraceContext traceContext) {
        super(connId, meta);
        this.traceContext = traceContext;
        this.startMicros = traceContext.rootSpan().getStartMicros();
        this.requestTimeMicros =
                traceContext.getRequestTimeMicros() + (this.startMicros - traceContext.getStartMicros());
    }

    public TraceContext getTraceContext() {
        return this.traceContext;
    }

    public void afterQueue() {
        this.waitInQueueMicros = System.nanoTime() / 1000L - this.startMicros;
    }

    public long getWaitInQueueMicros() {
        return this.waitInQueueMicros;
    }

    public boolean isReplied() {
        return this.replied.get();
    }

    public boolean setReplied() {
        return this.replied.compareAndSet(false, true);
    }

    public long getDecodeMicros() {
        return this.decodeMicros;
    }

    public void setDecodeMicros(long decodeMicros) {
        this.decodeMicros = decodeMicros;
    }
}
