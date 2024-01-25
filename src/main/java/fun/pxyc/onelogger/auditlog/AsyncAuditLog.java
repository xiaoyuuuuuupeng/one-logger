package fun.pxyc.onelogger.auditlog;

import fun.pxyc.onelogger.ServerContextData;
import fun.pxyc.onelogger.trace.*;
import fun.pxyc.onelogger.utils.JsonUtil;
import fun.pxyc.onelogger.utils.TypeSafe;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncAuditLog {
    public static final Logger defaultLog = LoggerFactory.getLogger("console");
    public static final Logger dbAuditLog = LoggerFactory.getLogger("auditlog.client.db");
    public static final Logger redisAuditLog = LoggerFactory.getLogger("auditlog.client.redis");
    public static final Logger httpAuditLog = LoggerFactory.getLogger("auditlog.client.http");
    public static final Logger mqAuditLog = LoggerFactory.getLogger("auditlog.client.mq");
    public static final Logger kafkaAuditLog = LoggerFactory.getLogger("auditlog.client.kafka");
    public static final Logger emqAuditLog = LoggerFactory.getLogger("auditlog.client.emq");
    public static final Logger hbaseAuditLog = LoggerFactory.getLogger("auditlog.client.hbase");
    public static final Logger mongoAuditLog = LoggerFactory.getLogger("auditlog.client.mongo");
    public static final Logger mqRecvAuditLog = LoggerFactory.getLogger("auditlog.server.mq");
    public static final Logger kafkaRecvAuditLog = LoggerFactory.getLogger("auditlog.server.kafka");
    public static final Logger emqRecvAuditLog = LoggerFactory.getLogger("auditlog.server.emq");
    public static final Logger xxlAuditLog = LoggerFactory.getLogger("auditlog.server.xxl");
    public static final Logger webAuditLog = LoggerFactory.getLogger("auditlog.server.web");
    public static final Logger dubboConsumerAuditLog = LoggerFactory.getLogger("auditlog.client.dubbo");
    public static final Logger dubboProviderAuditLog = LoggerFactory.getLogger("auditlog.server.dubbo");
    public static final Logger bizProviderAuditLog = LoggerFactory.getLogger("auditlog.client.biz");
    public static final Logger konisGraphAuditLog = LoggerFactory.getLogger("auditlog.client.konisgraph");
    public static final Logger elasticSearchAuditLog = LoggerFactory.getLogger("auditlog.client.elasticsearch");
    public static DateTimeFormatter tsFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static ZoneOffset offset = OffsetDateTime.now().getOffset();

    public AsyncAuditLog() {}

    public static boolean isDbEnabled() {
        return dbAuditLog.isInfoEnabled();
    }

    public static void logDb(String s) {
        AsyncAuditLogPool.asyncLog(dbAuditLog, s);
    }

    public static boolean isBizEnabled() {
        return bizProviderAuditLog.isInfoEnabled();
    }

    public static void logBiz(String s) {
        AsyncAuditLogPool.asyncLog(bizProviderAuditLog, s);
    }

    public static boolean isRedisEnabled() {
        return redisAuditLog.isInfoEnabled();
    }

    public static void logRedis(String s) {
        AsyncAuditLogPool.asyncLog(redisAuditLog, s);
    }

    public static boolean isHttpEnabled() {
        return httpAuditLog.isInfoEnabled();
    }

    public static void logHttp(String s) {
        AsyncAuditLogPool.asyncLog(httpAuditLog, s);
    }

    public static boolean isMqEnabled() {
        return mqAuditLog.isInfoEnabled();
    }

    public static void logMq(String s) {
        AsyncAuditLogPool.asyncLog(mqAuditLog, s);
    }

    public static boolean isKafkaEnabled() {
        return kafkaAuditLog.isInfoEnabled();
    }

    public static void logKafka(String s) {
        AsyncAuditLogPool.asyncLog(kafkaAuditLog, s);
    }

    public static boolean isMqRecvEnabled() {
        return mqRecvAuditLog.isInfoEnabled();
    }

    public static void logMqRecv(String s) {
        AsyncAuditLogPool.asyncLog(mqRecvAuditLog, s);
    }

    public static boolean isKafkaRecvEnabled() {
        return kafkaRecvAuditLog.isInfoEnabled();
    }

    public static void logKafkaRecv(String s) {
        AsyncAuditLogPool.asyncLog(kafkaRecvAuditLog, s);
    }

    public static boolean isXxlEnabled() {
        return xxlAuditLog.isInfoEnabled();
    }

    public static void logXxl(String s) {
        AsyncAuditLogPool.asyncLog(xxlAuditLog, s);
    }

    public static boolean isHbaseEnabled() {
        return hbaseAuditLog.isInfoEnabled();
    }

    public static void logHbase(String s) {
        AsyncAuditLogPool.asyncLog(hbaseAuditLog, s);
    }

    public static boolean isEmqEnabled() {
        return emqAuditLog.isInfoEnabled();
    }

    public static void logEmq(String s) {
        AsyncAuditLogPool.asyncLog(emqAuditLog, s);
    }

    public static boolean isEmqRecvEnabled() {
        return emqRecvAuditLog.isInfoEnabled();
    }

    public static void logEmqRecv(String s) {
        AsyncAuditLogPool.asyncLog(emqRecvAuditLog, s);
    }

    public static boolean isMongoEnabled() {
        return mongoAuditLog.isInfoEnabled();
    }

    public static void logMongo(String s) {
        AsyncAuditLogPool.asyncLog(mongoAuditLog, s);
    }

    public static boolean isWebEnabled() {
        return webAuditLog.isInfoEnabled();
    }

    public static void logWeb(String s) {
        AsyncAuditLogPool.asyncLog(webAuditLog, s);
    }

    public static boolean isDubboConsumerAuditLogEnabled() {
        return dubboConsumerAuditLog.isInfoEnabled();
    }

    public static void logDubboConsumer(String s) {
        AsyncAuditLogPool.asyncLog(dubboConsumerAuditLog, s);
    }

    public static boolean isDubboProviderAuditLogEnabled() {
        return dubboProviderAuditLog.isInfoEnabled();
    }

    public static void logDubboProvider(String s) {
        AsyncAuditLogPool.asyncLog(dubboProviderAuditLog, s);
    }

    public static boolean isKonisGraphEnabled() {
        return konisGraphAuditLog.isInfoEnabled();
    }

    public static boolean isElasticSearchEnabled() {
        return elasticSearchAuditLog.isInfoEnabled();
    }

    public static long nowTs(TraceContext ctx, Span span) {
        long t = ctx.getRequestTimeMicros() + span.getStartMicros() - ctx.getStartMicros() + span.getTimeUsedMicros();
        return t;
    }

    public static String now(TraceContext ctx, Span span) {
        long t = ctx.getRequestTimeMicros() + span.getStartMicros() - ctx.getStartMicros() + span.getTimeUsedMicros();
        String ts = tsFormat.format(LocalDateTime.ofEpochSecond(t / 1000000L, (int) (t % 1000000L * 1000L), offset));
        return ts;
    }

    public static long nowTs(TraceContext ctx, long stopMicros) {
        long t = ctx.getRequestTimeMicros() + stopMicros - ctx.getStartMicros();
        return t;
    }

    public static String now(TraceContext ctx, long stopMicros) {
        long t = ctx.getRequestTimeMicros() + stopMicros - ctx.getStartMicros();
        String ts = tsFormat.format(LocalDateTime.ofEpochSecond(t / 1000000L, (int) (t % 1000000L * 1000L), offset));
        return ts;
    }

    public static String escapeText(String input) {
        return escapeText(input, 200);
    }

    public static Logger findLogger(String loggerName) {
        if (loggerName.isEmpty()) {
            return defaultLog;
        }
        switch (loggerName) {
            case "auditlog.client.db":
                return dbAuditLog;
            case "auditlog.client.redis":
                return redisAuditLog;
            case "auditlog.client.http":
                return httpAuditLog;
            case "auditlog.client.mq":
                return mqAuditLog;
            case "auditlog.client.kafka":
                return kafkaAuditLog;
            case "auditlog.client.emq":
                return emqAuditLog;
            case "auditlog.client.hbase":
                return hbaseAuditLog;
            case "auditlog.client.mongo":
                return mongoAuditLog;
            case "auditlog.server.mq":
                return mqRecvAuditLog;
            case "auditlog.server.kafka":
                return kafkaRecvAuditLog;
            case "auditlog.server.emq":
                return emqRecvAuditLog;
            case "auditlog.server.xxl":
                return xxlAuditLog;
            case "auditlog.server.web":
                return webAuditLog;
            case "auditlog.client.dubbo":
                return dubboConsumerAuditLog;
            case "auditlog.server.dubbo":
                return dubboProviderAuditLog;
            case "auditlog.client.konisgraph":
                return konisGraphAuditLog;
            case "auditlog.client.elasticsearch":
                return elasticSearchAuditLog;
            default:
                return defaultLog;
        }
    }

    public static String escapeText(String input, int max) {
        if (input != null && !input.isEmpty()) {
            boolean needEscape = false;
            char[] ca = input.toCharArray();
            char[] ca1 = ca;
            int n = ca.length;

            int i;
            char b;
            for (i = 0; i < n; ++i) {
                b = ca1[i];
                if (b < ' ') {
                    needEscape = true;
                    break;
                }
            }
            if (max == -1) {
                return input;
            }
            if (!needEscape && input.length() < max) {
                return input;
            } else {
                StringBuilder builder = new StringBuilder();
                n = 0;

                for (i = 0; i < ca.length; ++i) {
                    b = ca[i];
                    if (b >= ' ' && b != '^') {
                        builder.append(b);
                    } else {
                        builder.append(" ");
                    }

                    ++n;
                    if (n >= max) {
                        break;
                    }
                }

                if (n >= max) {
                    builder.append("...");
                }

                String s = builder.toString();
                if (s.contains(",   ")) {
                    s = s.replace(",   ", ",");
                }

                return s;
            }
        } else {
            return "";
        }
    }

    public static String bytesToTextAndEscape(byte[] bytes) {
        String s = "";

        try {
            s = new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            s = new String(bytes);
        }

        return escapeText(s);
    }

    public static String toTextAndEscape(Object o) {
        return escapeText(toText(o), 200);
    }

    public static String toTextAndEscape(Object o, int max) {
        return escapeText(toText(o), max);
    }

    public static String toText(Object o) {
        if (o == null) {
            return "";
        } else if (o instanceof String) {
            return (String) o;
        } else if (o instanceof Number) {
            return o.toString();
        } else if (o instanceof TimeUnit) {
            return o.toString();
        } else {
            String s;
            if (o instanceof Date) {
                s = o.toString();
                int p = s.lastIndexOf(".");
                return p >= 0 ? s.substring(0, p) : s;
            } else {
                s = JsonUtil.toJson(o);
                return s == null ? "" : s;
            }
        }
    }

    public static String mapToString(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            sb.append(key).append(":").append(toTextAndEscape(value, -1)).append("^_^");
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 3, sb.length());
        }
        return sb.toString();
    }

    public static TraceData newTrace(String peers) {
        TraceData traceData = new TraceData();
        TraceIds traceIds = Trace.newStartTraceIds(true);
        traceData.setTraceId(traceIds.getTraceId());
        traceData.setParentSpanId(traceIds.getParentSpanId());
        traceData.setSpanId(traceIds.getSpanId());
        traceData.setSampleFlag(Trace.getSampleFlag());
        traceData.setPeers(peers);
        return traceData;
    }

    public static void appendVars(ServerContextData serverCtx, StringBuilder extraInfo) {
        Map<String, Object> attrs = serverCtx.getAttributes();
        if (attrs != null) {
            Iterator iterator = attrs.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
                String key = entry.getKey();
                if (key.startsWith("var:")) {
                    key = key.substring(4);
                    String value = TypeSafe.anyToString(entry.getValue());
                    if (extraInfo.length() > 0) {
                        extraInfo.append("^");
                    }
                    extraInfo.append(key).append(":").append(value);
                }
            }
        }
    }

    public static void appendTimeUsed(ServerContextData serverCtx, StringBuilder extraInfo, long ttl) {
        TraceContext tc = serverCtx.getTraceContext();
        if (tc != null) {
            if (tc instanceof DefaultTraceContext) {
                DefaultTraceContext dtc = (DefaultTraceContext) tc;
                String timeUsedStr = dtc.getTimeUsedStr();
                if (timeUsedStr != null && !timeUsedStr.isEmpty()) {
                    int p = timeUsedStr.indexOf("^IOSUM:");
                    long ot = 0L;
                    if (p > 0) {
                        ot = ttl - TypeSafe.anyToLong(timeUsedStr.substring(p + 7));
                        if (ot < 0L) {
                            ot = 0L;
                        }

                        timeUsedStr = timeUsedStr.substring(0, p);
                    }

                    if (extraInfo.length() > 0) {
                        extraInfo.append("^");
                    }

                    extraInfo.append(timeUsedStr);
                    extraInfo.append("^OTS:").append(ot);
                }
            }
        }
    }
}
