package fun.pxyc.onelogger.elasticsearch;

import fun.pxyc.onelogger.auditlog.AsyncAuditLog;
import fun.pxyc.onelogger.autoconfig.EnvConfigProps;
import fun.pxyc.onelogger.log.Action;
import fun.pxyc.onelogger.log.LoggerFormatter;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import fun.pxyc.onelogger.trace.TraceData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

@Aspect
public class ElasticSearchAuditLogAspect extends LoggerFormatter {

    private static final Logger log = LoggerFactory.getLogger(ElasticSearchAuditLogAspect.class);

    private final AtomicInteger seq = new AtomicInteger(0);

    public ElasticSearchAuditLogAspect() {}

    @Pointcut("execution(* org.elasticsearch.client.RestClient.performRequest(org.elasticsearch.client.Request))")
    public void elasticSearchPointCut() {}

    @Around("elasticSearchPointCut()")
    public Object elasticSearchAuditAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        return this.doAdvice(joinPoint);
    }

    public Object doAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String addr = "";
        String action = joinPoint.getTarget().getClass().getSimpleName().toLowerCase() + "."
                + joinPoint.getSignature().getName().toLowerCase();
        Span span = Trace.startAsync("ElasticSearch", action);
        span.setRemoteAddr(addr);
        try {
            if (args[0] instanceof Request) {
                Request req = (Request) args[0];
                action = req.getEndpoint();
                span.changeAction(action);
                Object result = joinPoint.proceed(args);
                if (result instanceof Response) {
                    Response resp = (Response) result;
                    Map<String, Object> propertyMap = reflectObjToMap(resp);
                    if (!CollectionUtils.isEmpty(propertyMap) && propertyMap.containsKey("response")) {
                        Object response = propertyMap.get("response");
                        span.setRemoteAddr(propertyMap.getOrDefault("host", "").toString());
                        span.stop(true);
                        this.log(span, action, req, response, null);
                    }
                }
                return result;
            } else {
                return joinPoint.proceed(args);
            }
        } catch (Throwable t) {
            span.stop(true);
            this.log(span, action, (Request) args[0], null, t);
            throw t;
        }
    }

    private void log(Span span, String action, Request req, Object result, Throwable throwable) {
        try {
            this.logInternal(span, action, req, result, throwable);
        } catch (Throwable t) {
            log.error("logInternal exception", t);
        }
    }

    private void logInternal(Span span, String action, Request req, Object result, Throwable throwable) {
        if (span == null) {
            log.error("span is null in ElasticSearchAuditLogAspect");
        } else {
            if (AsyncAuditLog.isElasticSearchEnabled()) {
                TraceContext ctx = Trace.currentContext();
                TraceData trace = ctx.getTrace();
                String traceId = trace.getTraceId();
                String timestamp = AsyncAuditLog.now(ctx, span);
                String connId = getConnId();
                int sequence = this.seq.incrementAndGet();
                if (sequence >= 10000000) {
                    this.seq.compareAndSet(sequence, 0);
                }
                long duration = span.getTimeUsedMicros();
                String spanId = trace.getSpanId();
                String serviceNameMsgName = "ElasticSearch." + action;
                Map<String, Object> reqParams = new LinkedHashMap<>();
                reqParams.put("req", getRequestParams(req));
                Log(
                        AsyncAuditLog.elasticSearchAuditLog,
                        generateLogMap(
                                timestamp,
                                traceId,
                                spanId,
                                span.getRootSpan().getAction(),
                                connId,
                                new Action("ElasticSearch", action, action, serviceNameMsgName),
                                sequence,
                                duration / 1000 + "ms",
                                reqParams,
                                getResponseRes(result),
                                genException(throwable),
                                EnvConfigProps.esLogMaxChars));
            }
        }
    }

    public String getRequestParams(Request request) {
        String requestLine = "";
        try {
            if (request.getEntity() != null) {
                HttpEntity entity = request.getEntity();
                if (!entity.isRepeatable()) {
                    entity = new BufferedHttpEntity(entity);
                    request.setEntity(entity);
                }
                requestLine += EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
            return requestLine;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> getResponseRes(Object response) {
        StringBuilder responseLine = new StringBuilder();
        try {
            if (response instanceof HttpResponse) {
                HttpResponse httpResponse = (HttpResponse) response;
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    if (!entity.isRepeatable()) {
                        entity = new BufferedHttpEntity(entity);
                    }
                    httpResponse.setEntity(entity);
                    ContentType contentType = ContentType.get(entity);
                    Charset charset = StandardCharsets.UTF_8;
                    if (contentType != null && contentType.getCharset() != null) {
                        charset = contentType.getCharset();
                    }
                    try (BufferedReader reader =
                            new BufferedReader(new InputStreamReader(entity.getContent(), charset))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseLine.append(line);
                        }
                    }
                }
            }
            return getJsonRes(responseLine.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> reflectObjToMap(Object obj) throws IllegalAccessException {
        Class<?> traversalClass = obj.getClass();
        Field[] declaredFields = traversalClass.getDeclaredFields();
        Map<String, Object> propertyMap = new HashMap<>();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            propertyMap.put(field.getName(), field.get(obj));
        }
        return propertyMap;
    }
}
