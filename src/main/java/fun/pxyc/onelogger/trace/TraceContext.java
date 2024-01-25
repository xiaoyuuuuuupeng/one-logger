package fun.pxyc.onelogger.trace;

import java.util.Map;

public interface TraceContext {
    Span startForServer(String type, String action);

    Span stopForServer(int stopCode);

    Span stopForServer(int retCode, String retMsg);

    Span rootSpan();

    Span startAsync(String type, String action);

    Span appendSpan(String type, String action, long startMicros, String status, long timeUsedMicros);

    Span start(String type, String action);

    void tagForRpc(String key, String value);

    void tagForRpcIfAbsent(String key, String value);

    String getTagForRpc(String key);

    String getTagsForRpc();

    TraceData getTrace();

    long getThreadId();

    String getThreadName();

    String getThreadGroupName();

    long getRequestTimeMicros();

    long getStartMicros();

    Map<String, String> getTagsMapForRpc();

    TraceContext detach();
}
