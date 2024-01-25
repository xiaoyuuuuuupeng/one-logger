package fun.pxyc.onelogger.kafka;

import fun.pxyc.onelogger.auditlog.AsyncAuditLog;
import fun.pxyc.onelogger.autoconfig.EnvConfigProps;
import fun.pxyc.onelogger.log.Action;
import fun.pxyc.onelogger.log.LoggerFormatter;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import fun.pxyc.onelogger.trace.TraceData;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Aspect
public class KafkaAuditLogSendAspect extends LoggerFormatter {
    private static final Logger log = LoggerFactory.getLogger(KafkaAuditLogSendAspect.class);
    private final AtomicInteger seq = new AtomicInteger(0);

    public KafkaAuditLogSendAspect() {}

    @Pointcut(
            "execution(* org.springframework.kafka.core.KafkaTemplate.send(..))||execution(* org.springframework.kafka.core.KafkaTemplate.sendDefault(..))")
    public void kafkaAuditLogSendPointCut() {}

    @Around("kafkaAuditLogSendPointCut()")
    public Object kafkaAuditLogSendAdvice(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Span span = Trace.startAsync(
                "KAFKA.send",
                joinPoint.getTarget().getClass().getSimpleName().toLowerCase() + "."
                        + joinPoint.getSignature().getName());
        final TraceContext ctx = Trace.currentContext();
        Object result = null;
        try {
            result = joinPoint.proceed();
            span.stop(true);
            ListenableFuture<SendResult<String, String>> future = (ListenableFuture) result;
            future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                public void onFailure(Throwable throwable) {
                    long now = System.nanoTime() / 1000L;
                    KafkaAuditLogSendAspect.this.log(span, now, ctx, joinPoint, throwable);
                    String targetAddrs = KafkaInstrument.getTargetAddrs((KafkaTemplate) joinPoint.getTarget());
                    // TODO 对接告警 ？？？
                    KafkaErrorCounter.addErrorCount(targetAddrs);
                }

                public void onSuccess(SendResult<String, String> stringStringSendResult) {
                    long now = System.nanoTime() / 1000L;
                    KafkaAuditLogSendAspect.this.log(span, now, ctx, joinPoint, null);
                }
            });
            return result;
        } catch (Throwable t) {
            span.stop(false);
            long now = System.nanoTime() / 1000L;
            this.log(span, now, ctx, joinPoint, t);
            String targetAddrs = KafkaInstrument.getTargetAddrs((KafkaTemplate) joinPoint.getTarget());
            KafkaErrorCounter.addErrorCount(targetAddrs);
            throw t;
        }
    }

    private void log(Span span, long now, TraceContext ctx, ProceedingJoinPoint joinPoint, Throwable e) {
        try {
            this.logInternal(span, now, ctx, joinPoint, e);
        } catch (Exception ex) {
            log.error("logInternal exception", ex);
        }
    }

    private void logInternal(Span span, long now, TraceContext ctx, ProceedingJoinPoint joinPoint, Throwable e) {
        Action action = new Action(
                "KAFKA.send",
                joinPoint.getTarget().getClass().getName(),
                joinPoint.getSignature().getName(),
                "KAFKA.send");
        Object[] args = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        String defaultTopic = ((KafkaTemplate) joinPoint.getTarget()).getDefaultTopic();
        TraceData trace = ctx.getTrace();
        long duration = span.getTimeUsedMicros();
        long fullDuration = now - span.getStartMicros();
        String traceId = trace.getTraceId();
        if (duration >= EnvConfigProps.slowKafkaMillis * 1000L) {
            log.warn("slow kafka: ts=" + duration + ", traceId=" + traceId);
        }
        if (AsyncAuditLog.isKafkaEnabled()) {
            String timestamp = AsyncAuditLog.now(ctx, now);
            String connId = this.genConnId(joinPoint);
            int sequence = this.seq.incrementAndGet();
            if (sequence >= 10000000) {
                this.seq.compareAndSet(sequence, 0);
            }

            String spanId = span.getSpanId();
            String rootSpan = span.getRootSpan().getAction();
            String extraInfo = "";
            extraInfo = extraInfo + this.genTopicName(args, paramNames, defaultTopic);
            extraInfo = extraInfo + "^aftercallback:" + fullDuration;
            Log(
                    AsyncAuditLog.kafkaAuditLog,
                    generateLogMapWithExtraInfo(
                            timestamp,
                            traceId,
                            spanId,
                            rootSpan,
                            connId,
                            action,
                            sequence,
                            span.getTimeUsedMs(),
                            this.genReqParams(args, paramNames),
                            this.genResParams(),
                            genException(e),
                            extraInfo,
                            EnvConfigProps.kafkaLogMaxChars));
        }
    }

    private String genConnId(ProceedingJoinPoint joinPoint) {
        String targetAddrs = KafkaInstrument.getTargetAddrs((KafkaTemplate) joinPoint.getTarget());
        if (targetAddrs == null || targetAddrs.isEmpty()) {
            return "0.0.0.0:0:0";
        }
        return targetAddrs;
    }

    private String genTopicName(Object[] args, String[] paramNames, String defaultTopic) {
        StringBuilder builder = new StringBuilder();
        builder.append("topic:");
        String topic = null;

        for (int i = 0; i < paramNames.length; ++i) {
            if (args[i] instanceof GenericMessage) {
                GenericMessage gm = (GenericMessage) args[i];
                topic = (String) gm.getHeaders().get("kafka_topic");
                break;
            }

            if ("topic".equalsIgnoreCase(paramNames[i])) {
                topic = args[i].toString();
                break;
            }
        }

        if (topic == null || topic.isEmpty()) {
            topic = defaultTopic;
        }

        if (topic == null || topic.isEmpty()) {
            topic = "default";
        }

        builder.append(topic);
        return builder.toString();
    }

    private Map<String, Object> genReqParams(Object[] args, String[] paramNames) {
        Map<String, Object> req = new HashMap<>();
        for (int i = 0; i < args.length; ++i) {
            if (!"topic".equals(paramNames[i])) {
                req.put(paramNames[i], AsyncAuditLog.toTextAndEscape(args[i]));
            }
        }
        return req;
    }

    private Map<String, Object> genResParams() {
        return new HashMap<>();
    }
}
