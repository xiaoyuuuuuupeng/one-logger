package fun.pxyc.onelogger.trace;

import fun.pxyc.onelogger.trace.adpater.TraceAdapter;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class Trace {
    private static final Logger log = LoggerFactory.getLogger(Trace.class);
    private static final ThreadLocal<TraceContext> tlContext = new ThreadLocal();
    private static final Random rand = new Random();
    private static String appName = "unknown";
    private static int sampleRate = 100;
    private static TraceAdapter adapter = null;

    public Trace() {}

    public static int getSampleFlag() {
        int sampleFlag = rand.nextInt(100) < getSampleRate() ? 0 : 2;
        return sampleFlag;
    }

    public static Span startForServer(TraceData trace, String type, String action) {
        TraceContext ctx = adapter.newTraceContext(trace, type.equals("RPCSERVER"));
        setCurrentContext(ctx);
        return ctx.startForServer(type, action);
    }

    public static Span start(String type, String action) {
        TraceContext ctx = getOrNew();
        return ctx.start(type, action);
    }

    public static Span startAsync(String type, String action) {
        TraceContext ctx = getOrNew();
        return ctx.startAsync(type, action);
    }

    public static Span appendSpan(String type, String action, long startMicros, String status, long timeUsedMicros) {
        TraceContext ctx = getOrNew();
        return ctx.appendSpan(type, action, startMicros, status, timeUsedMicros);
    }

    private static TraceContext getOrNew() {
        TraceContext ctx = tlContext.get();
        if (ctx != null) {
            return ctx;
        } else {
            ctx = adapter.newTraceContext();
            setCurrentContext(ctx);
            MDC.put("TRACE_ID", ctx.getTrace().traceId);
            return ctx;
        }
    }

    public static void setCurrentContext(TraceContext traceContext) {
        tlContext.set(traceContext);
    }

    public static void tagForRpc(String key, String value) {
        TraceContext ctx = tlContext.get();
        if (ctx != null) {
            ctx.tagForRpc(key, value);
        }
    }

    public static void tagForRpcIfAbsent(String key, String value) {
        if (getTagForRpc(key) == null) {
            tagForRpc(key, value);
        }
    }

    public static String getTagForRpc(String key) {
        TraceContext ctx = tlContext.get();
        return ctx == null ? null : ctx.getTagForRpc(key);
    }

    public static TraceIds newStartTraceIds(boolean isServerSide) {
        return adapter.newStartTraceIds(isServerSide);
    }

    public static void inject(TraceContext ctx, Span span, TraceData traceData) {
        adapter.inject(ctx, span, traceData);
    }

    public static TraceContext currentContext() {
        return tlContext.get();
    }

    public static void restoreContext(TraceContext traceContext) {
        tlContext.set(traceContext);
    }

    public static void restoreDetachedContext(TraceContext traceContext) {
        if (traceContext == null) {
            tlContext.set(null);
        }

        tlContext.set(traceContext.detach());
    }

    public static void clearContext() {
        tlContext.set(null);
    }

    public static TraceAdapter getAdapter() {
        return adapter;
    }

    public static void setAdapter(TraceAdapter adapter) {
        Trace.adapter = adapter;
    }

    public static String getAppName() {
        return appName;
    }

    public static void setAppName(String appName) {
        Trace.appName = appName;
    }

    public static int getSampleRate() {
        return sampleRate;
    }

    public static void setSampleRate(int sampleRate) {
        Trace.sampleRate = sampleRate;
    }
}
