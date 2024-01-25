package fun.pxyc.onelogger.trace.adpater;

import fun.pxyc.onelogger.trace.*;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultTraceAdapter implements TraceAdapter {

    public void send(TraceContext ctx, Span span) {
        // do nothing
    }

    @Override
    public boolean hasError(int retCode) {
        return retCode < 0;
    }

    public void inject(TraceContext ctx, Span span, TraceData traceData) {
        traceData.setTraceId(ctx.getTrace().getTraceId());
        traceData.setParentSpanId("");
        traceData.setSpanId(span.getSpanId());
        traceData.setTags(ctx.getTagsForRpc());
    }

    public SpanIds restore(String parentSpanId, String spanId) {
        return null;
    }

    // like ali eagle style
    public TraceIds newStartTraceIds(boolean isServer) {
        String traceId = newTraceId();
        if (isServer) return new TraceIds(traceId, "", "0.1");
        else return new TraceIds(traceId, "", "");
    }

    public SpanIds newChildSpanIds(String spanId, AtomicInteger subCalls) {
        if (isEmpty(spanId)) spanId = "0";
        String childId = spanId + "." + subCalls.incrementAndGet(); // 0.1.1.1
        return new SpanIds(spanId, childId);
    }

    String newTraceId() {
        String s = UUID.randomUUID().toString();
        return s.replaceAll("-", "");
    }

    boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
