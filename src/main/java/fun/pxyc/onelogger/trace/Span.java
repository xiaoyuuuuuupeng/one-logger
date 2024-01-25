package fun.pxyc.onelogger.trace;

import java.util.List;
import java.util.Map;

public interface Span {
    Span newChild(String type, String action);

    Span newChild(String type, String action, long startMicros);

    long stop();

    long stop(boolean ok);

    long stop(String status);

    long stopForServer(String status);

    long stopWithTime(String status, long startMicros);

    void logEvent(String type, String action, String status, String data);

    void logException(Throwable t);

    void logException(String message, Throwable cause);

    void tag(String key, String value);

    void incCount(String key);

    void incQuantity(String key, long value);

    void incSum(String key, double value);

    void incQuantitySum(String key, long v1, double v2);

    Span getRootSpan();

    String getRootSpanId();

    SpanIds getSpanIds();

    String getParentSpanId();

    String getSpanId();

    String getType();

    String getAction();

    long getStartMicros();

    long getTimeUsedMicros();

    String getTimeUsedMs();

    String getStatus();

    String getRemoteAddr();

    void setRemoteAddr(String addr);

    List<Event> getEvents();

    Map<String, String> getTags();

    List<Metric> getMetrics();

    void removeTag(String key);

    void removeTags();

    List<Span> getChildren();

    String statsTimeUsed();

    void changeAction(String action);
}
