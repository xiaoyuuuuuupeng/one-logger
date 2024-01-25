package fun.pxyc.onelogger.konisgraph;

import fun.pxyc.onelogger.auditlog.AsyncAuditLog;
import fun.pxyc.onelogger.autoconfig.EnvConfigProps;
import fun.pxyc.onelogger.log.Action;
import fun.pxyc.onelogger.log.LoggerFormatter;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import fun.pxyc.onelogger.trace.TraceData;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.Bytecode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

@Aspect
public class KonisGraphAuditLogAspect extends LoggerFormatter {

    private static final Logger log = LoggerFactory.getLogger(KonisGraphAuditLogAspect.class);

    private final AtomicInteger seq = new AtomicInteger(0);

    public KonisGraphAuditLogAspect() {}

    @Pointcut(
            "execution(* org.apache.tinkerpop.gremlin.process.remote.RemoteConnection.submitAsync(org.apache.tinkerpop.gremlin.process.traversal.Bytecode))")
    public void konisGraphPointCut() {}

    @Around("konisGraphPointCut()")
    public Object konisGraphAuditAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        return this.doAdvice(joinPoint);
    }

    public Object doAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String addr = "";
        String action = joinPoint.getTarget().getClass().getSimpleName().toLowerCase() + "."
                + joinPoint.getSignature().getName().toLowerCase();
        Span span = Trace.startAsync("KONISGRAPH", action);
        span.setRemoteAddr(addr);

        try {
            Object result = joinPoint.proceed(args);
            span.stop(true);
            this.log(span, action, args[0], result, null);
            return result;
        } catch (Throwable t) {
            span.stop(true);
            this.log(span, action, args[0], null, t);
            throw t;
        }
    }

    private void log(Span span, String action, Object args, Object result, Throwable throwable) {
        try {
            this.logInternal(span, action, args, result, throwable);
        } catch (Throwable t) {
            log.error("logInternal exception", t);
        }
    }

    private void logInternal(Span span, String action, Object args, Object result, Throwable throwable) {
        if (span == null) {
            log.error("span is null in KonisGraphAuditLogAspect");
        } else {
            if (AsyncAuditLog.isKonisGraphEnabled() && args instanceof Bytecode) {
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
                String serviceNameMsgName = "KONISGRAPH." + action;
                StringBuilder extraInfo = new StringBuilder("g.");
                Bytecode bytecode = (Bytecode) args;
                List<Bytecode.Instruction> stepInstructions = bytecode.getStepInstructions();
                stepInstructions.forEach(step -> extraInfo.append(step).append("."));
                extraInfo.deleteCharAt(extraInfo.length() - 1);
                Map<String, Object> reqParams = new LinkedHashMap<>();
                reqParams.put("req", extraInfo.toString());
                Log(
                        AsyncAuditLog.konisGraphAuditLog,
                        generateLogMap(
                                timestamp,
                                traceId,
                                spanId,
                                span.getRootSpan().getAction(),
                                connId,
                                new Action("KONISGRAPH", serviceNameMsgName),
                                sequence,
                                duration / 1000 + "ms",
                                reqParams,
                                getFutureJsonRes(result),
                                genException(throwable),
                                EnvConfigProps.konisGraphLogMaxChars));
            }
        }
    }

    public Map<String, Object> getFutureJsonRes(Object retObj) {
        List<String> targetResults = new ArrayList<>();
        if (retObj instanceof CompletableFuture) {
            CompletableFuture r = (CompletableFuture) retObj;
            Object join = null;
            try {
                join = r.join();
                if (join instanceof DriverRemoteTraversal) {
                    DriverRemoteTraversal traversal = (DriverRemoteTraversal) join;
                    if (traversal.hasNext()) {
                        List<Result> results = reflectTraversal(traversal);
                        targetResults = results.stream().map(Result::getString).collect(Collectors.toList());
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return getJsonRes(targetResults);
        }
        return getJsonRes(targetResults);
    }

    private List<Result> reflectTraversal(DriverRemoteTraversal traversal) throws IllegalAccessException {
        List<Result> resultList = new ArrayList<>();
        Map<String, Object> propertyMap = reflectObjToMap(traversal);
        if (!CollectionUtils.isEmpty(propertyMap) && propertyMap.containsKey("traversers")) {
            Object traverser = propertyMap.get("traversers");
            Map<String, Object> traverserPropertyMap = reflectObjToMap(traverser);
            if (!CollectionUtils.isEmpty(traverserPropertyMap) && traverserPropertyMap.containsKey("inner")) {
                Object inner = traverserPropertyMap.get("inner");
                Map<String, Object> innerPropertyMap = reflectObjToMap(inner);
                if (!CollectionUtils.isEmpty(innerPropertyMap)) {
                    if (innerPropertyMap.containsKey("nextOne")) {
                        if (innerPropertyMap.get("nextOne") instanceof Result) {
                            Result nextOne = (Result) innerPropertyMap.get("nextOne");
                            resultList.add(nextOne);
                        }
                    }
                    if (innerPropertyMap.containsKey("this$0")) {
                        Object special = innerPropertyMap.get("this$0");
                        Map<String, Object> specialPropertyMap = reflectObjToMap(special);
                        if (!CollectionUtils.isEmpty(specialPropertyMap)
                                && specialPropertyMap.containsKey("resultQueue")) {
                            Object resultQueue = specialPropertyMap.get("resultQueue");
                            Map<String, Object> resultQueueProperty = reflectObjToMap(resultQueue);
                            if (!CollectionUtils.isEmpty(resultQueueProperty)
                                    && resultQueueProperty.containsKey("resultLinkedBlockingQueue")) {
                                if (resultQueueProperty.get("resultLinkedBlockingQueue")
                                        instanceof LinkedBlockingQueue) {
                                    LinkedBlockingQueue<Result> resultLinkedBlockingQueue =
                                            (LinkedBlockingQueue<Result>)
                                                    resultQueueProperty.get("resultLinkedBlockingQueue");
                                    resultList.addAll(resultLinkedBlockingQueue);
                                }
                            }
                        }
                    }
                }
            }
        }
        return resultList;
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
