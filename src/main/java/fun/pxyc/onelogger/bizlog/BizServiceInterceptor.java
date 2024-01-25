package fun.pxyc.onelogger.bizlog;

import fun.pxyc.onelogger.auditlog.AsyncAuditLog;
import fun.pxyc.onelogger.autoconfig.EnvConfigProps;
import fun.pxyc.onelogger.log.Action;
import fun.pxyc.onelogger.log.LoggerFormatter;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import fun.pxyc.onelogger.trace.TraceData;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BizServiceInterceptor extends LoggerFormatter implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(BizServiceInterceptor.class);

    private final ConcurrentHashMap<String, AtomicInteger> seqMap = new ConcurrentHashMap<>();

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        TraceContext ctx = Trace.currentContext();
        if (ctx == null) {
            return methodInvocation.proceed();
        }
        String methodName = methodInvocation.getMethod().getName();
        String className = methodInvocation.getMethod().getDeclaringClass().getName();
        Span span = Trace.startAsync("BIZ", className.toLowerCase() + "." + methodName.toLowerCase());
        try {
            Object o = methodInvocation.proceed();
            span.stop(true);
            this.log(span, ctx, methodInvocation, o, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(span, ctx, methodInvocation, null, t);
            throw t;
        }
    }

    private void log(Span span, TraceContext ctx, MethodInvocation methodInvocation, Object o, Throwable throwable) {
        try {
            this.logInternal(span, ctx, methodInvocation, o, throwable);
        } catch (Throwable t) {
            log.error("logInternal exception", t);
            log.error(t.getMessage(), t);
        }
    }

    private void logInternal(
            Span span, TraceContext ctx, MethodInvocation joinPoint, Object retObj, Throwable throwable) {
        String methodName = joinPoint.getMethod().getName();
        String className = joinPoint.getMethod().getDeclaringClass().getName();
        TraceData trace = ctx.getTrace();
        String costTime = span.getTimeUsedMs();
        String traceId = trace.getTraceId();
        String timestamp = AsyncAuditLog.now(ctx, span);

        String spanId = span.getSpanId();
        String serviceNameMsgName = "BIZ." + className + "." + methodName;
        AtomicInteger atomicInteger = seqMap.get(serviceNameMsgName);
        if (atomicInteger == null) {
            atomicInteger = new AtomicInteger(0);
            seqMap.put(serviceNameMsgName, atomicInteger);
        }
        int sequence = atomicInteger.incrementAndGet();
        if (sequence >= 10000000) {
            atomicInteger.compareAndSet(sequence, 0);
        }

        Log(
                AsyncAuditLog.bizProviderAuditLog,
                generateLogMap(
                        timestamp,
                        traceId,
                        spanId,
                        span.getRootSpan().getAction(),
                        getConnId(),
                        new Action("BIZ", className, methodName, serviceNameMsgName),
                        sequence,
                        costTime,
                        getJsonReq(joinPoint),
                        getJsonRes(retObj),
                        genException(throwable),
                        EnvConfigProps.bizLogMaxChars));
    }
}
