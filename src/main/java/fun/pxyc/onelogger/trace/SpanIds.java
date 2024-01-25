package fun.pxyc.onelogger.trace;

public class SpanIds {
    private String parentSpanId;
    private String spanId;

    public SpanIds(String parentSpanId, String spanId) {
        this.parentSpanId = parentSpanId;
        this.spanId = spanId;
    }

    public String getParentSpanId() {
        return this.parentSpanId;
    }

    public void setParentSpanId(String parentSpanId) {
        this.parentSpanId = parentSpanId;
    }

    public String getSpanId() {
        return this.spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }
}
