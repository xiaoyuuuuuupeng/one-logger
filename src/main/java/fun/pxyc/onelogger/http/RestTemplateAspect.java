package fun.pxyc.onelogger.http;

import fun.pxyc.onelogger.auditlog.AsyncAuditLog;
import fun.pxyc.onelogger.autoconfig.EnvConfigProps;
import fun.pxyc.onelogger.log.Action;
import fun.pxyc.onelogger.log.LoggerFormatter;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import fun.pxyc.onelogger.trace.TraceData;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

@Aspect
public class RestTemplateAspect extends LoggerFormatter {
    private static final Logger log = LoggerFactory.getLogger(RestTemplateAspect.class);
    private static final Set<String> logHeaderNames = new HashSet();
    public static boolean logAllHeaders = false;

    static {
        logHeaderNames.add("content-type");
        logHeaderNames.add("content-length");
    }

    private final AtomicInteger seq = new AtomicInteger(0);

    public RestTemplateAspect() {}

    public static void initLogHeaderNames(String s) {
        if (s != null && !s.isEmpty()) {
            String[] ss = s.split(",");
            String[] strings = ss;
            int length = ss.length;

            for (int i = 0; i < length; ++i) {
                String t = strings[i];
                logHeaderNames.add(t.toLowerCase());
            }
        }
    }

    @Pointcut(
            "execution(* org.springframework.web.client.RestTemplate.exchange(..))||execution(* org.springframework.web.client.RestTemplate.getFor*(..))||execution(* org.springframework.web.client.RestTemplate.headFor*(..))||execution(* org.springframework.web.client.RestTemplate.postFor*(..))||execution(* org.springframework.web.client.RestTemplate.put*(..))||execution(* org.springframework.web.client.RestTemplate.delete*(..))")
    public void restTemplatePointCut() {}

    @Around("restTemplatePointCut()")
    public Object restTemplateAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Span span = Trace.startAsync("HTTP", "SEND");
        TraceContext ctx = Trace.currentContext();
        HttpCallInfo callInfo = new HttpCallInfo();
        HttpCallInfo.instance.set(callInfo);

        try {
            Object o = joinPoint.proceed();
            span.stop(true);
            this.log(span, ctx, joinPoint, o, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            callInfo.httpCode = 999;
            this.log(span, ctx, joinPoint, null, t);
            throw t;
        }
    }

    private void log(Span span, TraceContext ctx, ProceedingJoinPoint joinPoint, Object o, Throwable throwable) {
        try {
            this.logInternal(span, ctx, joinPoint, o, throwable);
        } catch (Throwable t) {
            log.error("logInternal exception", t);
        }
    }

    private void logInternal(
            Span span, TraceContext ctx, ProceedingJoinPoint joinPoint, Object retObj, Throwable throwable) {
        HttpCallInfo callInfo = HttpCallInfo.instance.get();
        if (callInfo != null) {
            TraceData trace = ctx.getTrace();
            long duration = span.getTimeUsedMicros();
            String timeUsedMs = span.getTimeUsedMs();
            String traceId = trace.getTraceId();
            String path = callInfo.uri.getRawPath().toLowerCase();
            span.changeAction(path);
            if (duration >= EnvConfigProps.slowHttpMillis * 1000L) {
                log.warn("slow http request:" + callInfo.uri.toString() + ", ts=" + duration + ", traceId=" + traceId);
            }

            if (AsyncAuditLog.isHttpEnabled()) {
                String timestamp = AsyncAuditLog.now(ctx, span);
                int port = callInfo.uri.getPort();
                if (port == -1) {
                    if (callInfo.uri.getScheme() != null
                            && callInfo.uri.getScheme().compareToIgnoreCase("https") == 0) {
                        port = 443;
                    } else {
                        port = 80;
                    }
                }

                String connId = callInfo.uri.getHost() + ":" + port;
                int sequence = this.seq.incrementAndGet();
                if (sequence >= 10000000) {
                    this.seq.compareAndSet(sequence, 0);
                }

                String spanId = span.getSpanId();
                String serviceNameMsgName = "HTTP." + callInfo.uri;
                Log(
                        AsyncAuditLog.httpAuditLog,
                        generateLogMap(
                                timestamp,
                                traceId,
                                spanId,
                                span.getRootSpan().getAction(),
                                connId,
                                new Action("HTTP", serviceNameMsgName),
                                sequence,
                                timeUsedMs,
                                getJsonReq(callInfo),
                                getJsonRes(callInfo, retObj),
                                genException(throwable),
                                EnvConfigProps.restTemplateLogMaxChars));
            }
        }
    }

    Map<String, Object> getJsonReq(HttpCallInfo callInfo) {
        Map<String, Object> req = new LinkedHashMap<>();
        req.put("method", callInfo.method);
        String queryString = callInfo.uri.getQuery();
        if (queryString != null && !queryString.isEmpty()) {
            req.put("queryString", AsyncAuditLog.escapeText(queryString, -1));
        }

        if (callInfo.reqHeaders != null) {
            Iterator iterator = callInfo.reqHeaders.entrySet().iterator();
            label44:
            while (true) {
                Map.Entry entry;
                String lkey;
                do {
                    do {
                        if (!iterator.hasNext()) {
                            break label44;
                        }
                        entry = (Map.Entry) iterator.next();
                        lkey = ((String) entry.getKey()).toLowerCase();
                    } while (!logAllHeaders && !logHeaderNames.contains(lkey));
                } while (lkey.equals("content-length")
                        && callInfo.method.equals("GET")
                        && entry.getValue().equals("0"));
                req.put((String) entry.getKey(), AsyncAuditLog.escapeText((String) entry.getValue()));
            }
        }

        if (callInfo.reqBodyStr != null && !callInfo.reqBodyStr.isEmpty()) {
            req.put("body", AsyncAuditLog.escapeText(callInfo.reqBodyStr, -1));
        }
        return req;
    }

    Map<String, Object> getJsonRes(HttpCallInfo callInfo, Object retObj) {
        Map<String, Object> res = new LinkedHashMap<>();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("httpCode", callInfo.httpCode);
        if (callInfo.resHeaders != null) {
            Iterator iterator = callInfo.resHeaders.entrySet().iterator();
            label32:
            while (true) {
                Map.Entry entry;
                do {
                    if (!iterator.hasNext()) {
                        break label32;
                    }

                    entry = (Map.Entry) iterator.next();
                } while (!logAllHeaders && !logHeaderNames.contains(((String) entry.getKey()).toLowerCase()));
                result.put((String) entry.getKey(), AsyncAuditLog.escapeText((String) entry.getValue()));
            }
        }

        if (retObj != null) {
            if (retObj instanceof ResponseEntity) {
                ResponseEntity re = (ResponseEntity) retObj;
                retObj = re.getBody();
            }
            result.put("body", AsyncAuditLog.toTextAndEscape(retObj, -1));
        }
        res.put("result", result);
        return res;
    }
}
