package fun.pxyc.onelogger.trace;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultTraceContext implements TraceContext {
    final Deque<Span> stack = new ArrayDeque<>();
    TraceData trace;
    long requestTimeMicros = System.currentTimeMillis() * 1000L;
    long startMicros = System.nanoTime() / 1000L;
    AtomicInteger subCalls = new AtomicInteger();
    Map<String, String> tagsForRpc;
    long threadId;
    String threadName = "";
    String threadGroupName = "";
    String timeUsedStr = "";

    public DefaultTraceContext(TraceData trace, boolean restoreFlag) {
        this.initThreadNames();
        this.trace = trace;
        if (restoreFlag) {
            SpanIds newSpanIds = Trace.getAdapter().restore(trace.getParentSpanId(), trace.getSpanId());
            if (newSpanIds != null) {
                TraceData data = new TraceData();
                data.setParentSpanId(newSpanIds.getParentSpanId());
                data.setSpanId(newSpanIds.getSpanId());
                this.trace = data;
            }
        }
    }

    public DefaultTraceContext() {
        this.initThreadNames();
        TraceData traceData = new TraceData();
        TraceIds ids = Trace.getAdapter().newStartTraceIds(false);
        traceData.setTraceId(ids.getTraceId());
        traceData.setParentSpanId(ids.getParentSpanId());
        traceData.setSpanId(ids.getSpanId());
        traceData.setSampleFlag(Trace.getSampleFlag());
        this.trace = traceData;
    }

    public TraceContext detach() {
        DefaultTraceContext newCtx = new DefaultTraceContext();
        newCtx.trace = this.trace;
        newCtx.requestTimeMicros = this.requestTimeMicros;
        newCtx.startMicros = this.startMicros;
        newCtx.threadId = this.threadId;
        newCtx.threadName = this.threadName;
        newCtx.threadGroupName = this.threadGroupName;
        newCtx.subCalls = this.subCalls;
        if (this.tagsForRpc != null) {
            newCtx.tagsForRpc = new LinkedHashMap(this.tagsForRpc);
        }

        return newCtx;
    }

    public Span startForServer(String type, String action) {
        SpanIds spanIds = new SpanIds(this.trace.getParentSpanId(), this.trace.getSpanId());
        Span rootSpan = new DefaultSpan(this, spanIds, type, action, this.startMicros, this.subCalls);
        synchronized (this.stack) {
            this.stack.addLast(rootSpan);
        }

        if (!this.isEmpty(this.trace.getTags())) {
            this.parseTags(rootSpan, this.trace.getTags());
        }

        return rootSpan;
    }

    public Span startAsync(String type, String action) {
        Span tail;
        synchronized (this.stack) {
            tail = this.stack.peekLast();
        }

        if (tail == null) {
            SpanIds childIds = Trace.getAdapter().newChildSpanIds(this.trace.getSpanId(), this.subCalls);
            return new DefaultSpan(this, childIds, type, action, -1L, this.subCalls);
        } else {
            return tail.newChild(type, action);
        }
    }

    public Span appendSpan(String type, String action, long startMicros, String status, long timeUsedMicros) {
        Span tail;
        synchronized (this.stack) {
            tail = this.stack.peekLast();
        }

        DefaultSpan span;
        if (tail == null) {
            SpanIds childIds = Trace.getAdapter().newChildSpanIds(this.trace.getSpanId(), this.subCalls);
            span = new DefaultSpan(this, childIds, type, action, startMicros, this.subCalls);
        } else {
            span = (DefaultSpan) tail.newChild(type, action, startMicros);
        }

        span.stopWithTime(status, timeUsedMicros);
        return span;
    }

    public Span start(String type, String action) {
        Span child = this.startAsync(type, action);
        synchronized (this.stack) {
            this.stack.addLast(child);
            return child;
        }
    }

    public Span rootSpan() {
        synchronized (this.stack) {
            return this.stack.peekFirst();
        }
    }

    public Span stopForServer(int retCode) {
        return this.stopForServer(retCode, null);
    }

    public Span stopForServer(int retCode, String retMsg) {
        boolean hasError = Trace.getAdapter().hasError(retCode);
        String result = !hasError ? "SUCCESS" : "ERROR";
        Span span;
        synchronized (this.stack) {
            span = this.stack.peekFirst();
            this.stack.clear();
        }

        if (span != null) {
            span.tag("retCode", String.valueOf(retCode));
            if (retCode != 0) {
                if (retMsg == null) {
                    retMsg = "no retMsg";
                }

                span.tag("retMsg", retMsg);
            }

            span.stopForServer(result);
            this.statsTimeUsed(span);
            this.sendToTrace(span);
        }

        return span;
    }

    public void removeFromStack(Span span, boolean isRootSpan) {
        boolean needSendToTrace = false;
        synchronized (this.stack) {
            if (span == this.stack.peekLast()) {
                this.stack.removeLast();
            } else if (span == this.stack.peekFirst()) {
                this.stack.clear();
            }

            if (this.stack.isEmpty()) {
                needSendToTrace = true;
            }
        }

        if (isRootSpan) {
            if (needSendToTrace) {
                this.sendToTrace(span);
            }
        }
    }

    private void sendToTrace(Span span) {
        this.doSend(span);
    }

    private void statsTimeUsed(Span span) {
        if (span != null) {
            this.timeUsedStr = span.statsTimeUsed();
        }
    }

    private void doSend(Span span) {
        if (this.trace.getSampleFlag() != 2) {
            Trace.getAdapter().send(this, span);
        }
    }

    void parseTags(Span rootSpan, String tags) {
        String[] ss = tags.split("&");

        for (int i = 0; i < ss.length; ++i) {
            String s = ss[i];
            String[] tt = s.split("=");
            String key = tt[0];
            String value = this.decodeValue(tt[1]);
            rootSpan.tag(key, value);
            this.tagForRpc(key, value);
        }
    }

    public void tagForRpc(String key, String value) {
        if (this.tagsForRpc == null) {
            this.tagsForRpc = new HashMap();
        }

        this.tagsForRpc.put(key, value);
    }

    public void tagForRpcIfAbsent(String key, String value) {
        if (this.getTagForRpc(key) == null) {
            this.tagForRpc(key, value);
        }
    }

    public String getTagForRpc(String key) {
        return this.tagsForRpc == null ? null : this.tagsForRpc.get(key);
    }

    public String getTagsForRpc() {
        if (this.tagsForRpc == null) {
            return null;
        } else {
            StringBuilder b = new StringBuilder();

            Map.Entry entry;
            for (Iterator iterator = this.tagsForRpc.entrySet().iterator();
                    iterator.hasNext();
                    b.append((String) entry.getKey()).append("=").append(this.encodeValue((String) entry.getValue()))) {
                entry = (Map.Entry) iterator.next();
                if (b.length() > 0) {
                    b.append("&");
                }
            }

            return b.toString();
        }
    }

    public Map<String, String> getTagsMapForRpc() {
        return this.tagsForRpc == null ? null : this.tagsForRpc;
    }

    void initThreadNames() {
        if (Trace.getAdapter().needThreadInfo()) {
            Thread t = Thread.currentThread();
            this.threadId = t.getId();
            this.threadName = t.getName();
            this.threadGroupName = t.getThreadGroup().getName();
        }
    }

    String decodeValue(String value) {
        try {
            return URLDecoder.decode(value, "utf-8");
        } catch (Exception e) {
            return value;
        }
    }

    String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, "utf-8");
        } catch (Exception e) {
            return value;
        }
    }

    boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public long getRequestTimeMicros() {
        return this.requestTimeMicros;
    }

    public long getStartMicros() {
        return this.startMicros;
    }

    public long getThreadId() {
        return this.threadId;
    }

    public String getThreadName() {
        return this.threadName;
    }

    public String getThreadGroupName() {
        return this.threadGroupName;
    }

    public TraceData getTrace() {
        return this.trace;
    }

    public String getTimeUsedStr() {
        return this.timeUsedStr;
    }
}
