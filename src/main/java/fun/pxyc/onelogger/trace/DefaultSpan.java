package fun.pxyc.onelogger.trace;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultSpan implements Span {
    final Object childrenLock = new Object();
    private final Object parent;
    private final SpanIds spanIds;
    private final String type;
    long startMicros;
    String remoteAddr;
    Map<String, String> tags;
    List<Event> events;
    List<Metric> metrics;
    List<DefaultSpan> children;
    volatile long timeUsedMicros;
    volatile String status = "unset";
    AtomicInteger subCalls;
    private String action;

    DefaultSpan(
            Object parent,
            SpanIds spanIds,
            String type,
            String action,
            long startMicros,
            AtomicInteger parentSubCalls) {
        this.parent = parent;
        this.spanIds = spanIds;
        this.type = type;
        this.action = action;
        if (startMicros <= 0L) {
            this.startMicros = System.nanoTime() / 1000L;
        } else {
            this.startMicros = startMicros;
        }

        if (Trace.getAdapter().useCtxSubCalls()) {
            this.subCalls = parentSubCalls;
        }
    }

    public Span newChild(String type, String action) {
        return this.newChild(type, action, -1L);
    }

    public Span newChild(String type, String action, long startMicros) {
        if (this.subCalls == null) {
            this.subCalls = new AtomicInteger();
        }

        SpanIds childIds = Trace.getAdapter().newChildSpanIds(this.spanIds.getSpanId(), this.subCalls);
        DefaultSpan child = new DefaultSpan(this, childIds, type, action, startMicros, this.subCalls);
        synchronized (this.childrenLock) {
            if (this.children == null) {
                this.children = new ArrayList();
            }

            this.children.add(child);
            return child;
        }
    }

    public String toString() {
        String b = "parentSpanId=" + this.spanIds.getParentSpanId() + "," + "spanId="
                + this.spanIds.getSpanId() + "," + "type="
                + this.type + "," + "action="
                + this.action + "," + "startMicros="
                + this.startMicros + "," + "timeUsedMicros="
                + this.timeUsedMicros + "," + "status="
                + this.status + "," + "remoteAddr="
                + this.remoteAddr + "," + "tags="
                + this.tags + "," + "events="
                + this.events + ",";
        return b;
    }

    public long stop() {
        return this.stop("SUCCESS");
    }

    public long stop(boolean ok) {
        return this.stop(ok ? "SUCCESS" : "ERROR");
    }

    public long stop(String status) {
        return this.stopWithTime(status, System.nanoTime() / 1000L - this.startMicros);
    }

    public long stopWithTime(String status, long timeUsedMicros) {
        this.status = status;
        this.timeUsedMicros = timeUsedMicros;
        DefaultTraceContext ctx = this.getContext();
        ctx.removeFromStack(this, this.parent == ctx);
        return timeUsedMicros;
    }

    public long stopForServer(String status) {
        long t = System.nanoTime() / 1000L - this.startMicros;
        this.status = status;
        this.timeUsedMicros = t;
        return t;
    }

    public void logEvent(String type, String action, String status, String data) {
        if (this.events == null) {
            this.events = new ArrayList();
        }

        Event e = new Event(type, action, status, data);
        this.events.add(e);
    }

    public void logException(Throwable cause) {
        this.logException(null, cause);
    }

    public void logException(String message, Throwable cause) {
        if (this.events == null) {
            this.events = new ArrayList();
        }

        StringWriter sw = new StringWriter(1024);
        if (message != null) {
            sw.write(message);
            sw.write(32);
        }

        cause.printStackTrace(new PrintWriter(sw));
        this.logEvent("Exception", cause.getClass().getName(), "ERROR", sw.toString());
    }

    public void tag(String key, String value) {
        if (this.tags == null) {
            this.tags = new LinkedHashMap();
        }

        this.tags.put(key, value);
    }

    public void removeTag(String key) {
        if (this.tags != null) {
            this.tags.remove(key);
        }
    }

    public void incCount(String key) {
        if (this.metrics == null) {
            this.metrics = new ArrayList();
        }

        this.metrics.add(new Metric(key, 1, "1"));
    }

    public void incQuantity(String key, long value) {
        if (this.metrics == null) {
            this.metrics = new ArrayList();
        }

        this.metrics.add(new Metric(key, 2, String.valueOf(value)));
    }

    public void incSum(String key, double value) {
        if (this.metrics == null) {
            this.metrics = new ArrayList();
        }

        String s = String.format("%.2f", value);
        this.metrics.add(new Metric(key, 3, s));
    }

    public void incQuantitySum(String key, long v1, double v2) {
        if (this.metrics == null) {
            this.metrics = new ArrayList();
        }

        String s = String.format("%d,%.2f", v1, v2);
        this.metrics.add(new Metric(key, 4, s));
    }

    public Span getRootSpan() {
        return this.parent instanceof DefaultSpan ? ((DefaultSpan) this.parent).getRootSpan() : this;
    }

    public String getRootSpanId() {
        return this.parent instanceof DefaultSpan
                ? ((DefaultSpan) this.parent).getRootSpanId()
                : this.spanIds.getSpanId();
    }

    public DefaultTraceContext getContext() {
        return this.parent instanceof DefaultSpan
                ? ((DefaultSpan) this.parent).getContext()
                : (DefaultTraceContext) this.parent;
    }

    public String statsTimeUsed() {
        synchronized (this.childrenLock) {
            if (this.children != null) {
                StringBuilder b = new StringBuilder();
                long sum = 0L;
                String type;
                long t;
                for (Iterator<DefaultSpan> iterator = this.children.iterator();
                        iterator.hasNext();
                        b.append(type).append(":").append(t)) {
                    DefaultSpan child = iterator.next();
                    type = child.getType();
                    t = child.getTimeUsedMicros();
                    sum += t;
                    if (b.length() > 0) {
                        b.append("^");
                    }
                }

                b.append("^IOSUM").append(":").append(sum);
                return b.toString();
            } else {
                return "";
            }
        }
    }

    public List<Span> getChildren() {
        synchronized (this.childrenLock) {
            return this.children == null ? null : new ArrayList<>(this.children);
        }
    }

    public String getType() {
        return this.type;
    }

    public String getStatus() {
        return this.status;
    }

    public List<Event> getEvents() {
        return this.events;
    }

    public String getAction() {
        return this.action;
    }

    public long getStartMicros() {
        return this.startMicros;
    }

    public long getTimeUsedMicros() {
        return this.timeUsedMicros;
    }

    public String getTimeUsedMs() {
        String costTime = "";
        if (this.timeUsedMicros <= 100) {
            costTime = this.timeUsedMicros + "ns";
        } else {
            costTime = this.timeUsedMicros / 1000L + "ms";
        }
        return costTime;
    }

    public Map<String, String> getTags() {
        return this.tags;
    }

    public String getRemoteAddr() {
        return this.remoteAddr;
    }

    public void setRemoteAddr(String addr) {
        this.remoteAddr = addr;
    }

    public String getParentSpanId() {
        return this.spanIds.getParentSpanId();
    }

    public String getSpanId() {
        return this.spanIds.getSpanId();
    }

    public SpanIds getSpanIds() {
        return this.spanIds;
    }

    public List<Metric> getMetrics() {
        return this.metrics;
    }

    public void removeTags() {
        this.tags = null;
    }

    public void changeAction(String action) {
        this.action = action;
    }
}
