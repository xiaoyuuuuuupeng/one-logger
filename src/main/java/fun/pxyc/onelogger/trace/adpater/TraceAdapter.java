package fun.pxyc.onelogger.trace.adpater;

import fun.pxyc.onelogger.trace.*;
import java.util.concurrent.atomic.AtomicInteger;

public interface TraceAdapter {

    default TraceContext newTraceContext() {
        return new DefaultTraceContext();
    }

    default TraceContext newTraceContext(TraceData trace, boolean restoreFlag) {
        return new DefaultTraceContext(trace, restoreFlag);
    }

    default boolean needThreadInfo() {
        return false;
    }

    default boolean useCtxSubCalls() {
        return false;
    }

    void inject(TraceContext ctx, Span span, TraceData traceData);

    SpanIds restore(String parentSpanId, String spanId);

    TraceIds newStartTraceIds(boolean isServer);

    SpanIds newChildSpanIds(String spanId, AtomicInteger subCalls);

    void send(TraceContext ctx, Span span);

    boolean hasError(int retCode);
}
