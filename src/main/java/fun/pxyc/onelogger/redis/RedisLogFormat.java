package fun.pxyc.onelogger.redis;

import fun.pxyc.onelogger.auditlog.AsyncAuditLog;
import fun.pxyc.onelogger.autoconfig.EnvConfigProps;
import fun.pxyc.onelogger.log.Action;
import fun.pxyc.onelogger.log.LoggerFormatter;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.TraceContext;
import fun.pxyc.onelogger.trace.TraceData;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisLogFormat extends LoggerFormatter {
    private static final Logger log = LoggerFactory.getLogger(RedisLogFormat.class);

    public RedisLogFormat() {}

    public static void log(
            Object res, Span span, String methodName, TraceContext ctx, Throwable throwable, Object... args) {
        try {
            logeInternal(res, span, methodName, ctx, throwable, args);
        } catch (Throwable t) {
            log.error("logeInternal exception", t);
        }
    }

    private static void logeInternal(
            Object res, Span span, String methodName, TraceContext ctx, Throwable throwable, Object... args) {
        TraceData trace = ctx.getTrace();
        long duration = span.getTimeUsedMicros();
        String timeUsedMs = span.getTimeUsedMs();
        String traceId = trace.getTraceId();
        if (duration >= EnvConfigProps.slowRedisMillis * 1000L) {
            log.warn("slow redis: ts=" + duration + ", traceId=" + traceId);
        }
        String timestamp = AsyncAuditLog.now(ctx, span);
        int sequence = RedisConfig.seq.incrementAndGet();
        if (sequence >= 10000000) {
            RedisConfig.seq.compareAndSet(sequence, 0);
        }
        String serviceNameMsgName = RedisConfig.LOG_TYPE + methodName;
        Log(
                AsyncAuditLog.redisAuditLog,
                generateLogMap(
                        timestamp,
                        traceId,
                        span.getSpanId(),
                        span.getRootSpan().getAction(),
                        genConnId(),
                        new Action("REDIS", serviceNameMsgName),
                        sequence,
                        timeUsedMs,
                        genJsonReq(args),
                        genJsonRes(res),
                        genExceptionStr(throwable),
                        EnvConfigProps.redisLogMaxChars));
    }

    static String genConnId() {
        return "0.0.0.0:0:0";
    }

    static Map<String, Object> genJsonReq(Object... args) {
        Map<String, Object> req = new LinkedHashMap<>();
        StringBuilder builder = new StringBuilder();
        Object[] tmpArgs = args;

        for (int i = 0; i < args.length; ++i) {
            Object o = tmpArgs[i];
            String s = AsyncAuditLog.toText(o);
            if (builder.length() > 0) {
                builder.append("|");
            }
            builder.append(s);
        }
        String s = builder.toString();
        if (s.length() > 10000) {
            s = s.substring(0, 10000) + "....";
        }
        req.put("params", s);
        return req;
    }

    static Map<String, Object> genJsonRes(Object res) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (res != null) {
            result.put("result", res);
        }
        return result;
    }

    static String genExceptionStr(Throwable throwable) {
        if (throwable != null) {
            return AsyncAuditLog.escapeText(throwable.getMessage());
        }
        return "";
    }
}
