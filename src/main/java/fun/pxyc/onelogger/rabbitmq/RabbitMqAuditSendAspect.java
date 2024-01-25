package fun.pxyc.onelogger.rabbitmq;

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
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Aspect
public class RabbitMqAuditSendAspect extends LoggerFormatter {
    private static final Logger log = LoggerFactory.getLogger(RabbitMqAuditSendAspect.class);
    private final AtomicInteger seq = new AtomicInteger(0);

    public RabbitMqAuditSendAspect() {}

    @Pointcut("execution(* org.springframework.amqp.rabbit.core.RabbitTemplate.convertAndSend(..))")
    public void rabbitMqAuditSendPointCut() {}

    @Around("rabbitMqAuditSendPointCut()")
    public void rabbitMqAuditSendAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        Span span = Trace.startAsync("RABBITMQ", "send");
        TraceContext ctx = Trace.currentContext();
        try {
            joinPoint.proceed();
            span.stop(true);
            this.log(span, ctx, joinPoint, null);
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(span, ctx, joinPoint, throwable);
            throw throwable;
        }
    }

    private void log(Span span, TraceContext ctx, ProceedingJoinPoint joinPoint, Throwable throwable) {
        try {
            this.logInternal(span, ctx, joinPoint, throwable);
        } catch (Throwable t) {
            log.error("logInternal exception", t);
        }
    }

    private void logInternal(Span span, TraceContext ctx, ProceedingJoinPoint joinPoint, Throwable throwable) {
        Object[] args = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        String exchange = ((RabbitTemplate) joinPoint.getTarget()).getExchange();
        TraceData trace = ctx.getTrace();
        long duration = span.getTimeUsedMicros();
        String traceId = trace.getTraceId();
        if (duration >= (EnvConfigProps.slowMqMillis * 1000L)) {
            log.warn("slow mq: ts=" + duration + ", traceId=" + traceId);
        }
        if (AsyncAuditLog.isMqEnabled()) {
            String timestamp = AsyncAuditLog.now(ctx, span);
            String connId = this.genConnId();
            int sequence = this.seq.incrementAndGet();
            if (sequence >= 10000000) {
                this.seq.compareAndSet(sequence, 0);
            }
            String spanId = span.getSpanId();
            Action action = new Action("RABBITMQ.send", joinPoint);
            Map reqParams = this.genReqParams(args, paramNames);
            Map resParams = this.genResParams(throwable);
            Log(
                    AsyncAuditLog.mqAuditLog,
                    generateLogMapWithExtraInfo(
                            timestamp,
                            traceId,
                            spanId,
                            span.getRootSpan().getAction(),
                            connId,
                            action,
                            sequence,
                            span.getTimeUsedMs(),
                            reqParams,
                            resParams,
                            genException(throwable),
                            this.genExchangeName(args, paramNames, exchange),
                            EnvConfigProps.mqLogMaxChars));
        }
    }

    String genConnId() {
        return "0.0.0.0:0:0";
    }

    String genExchangeName(Object[] args, String[] paramNames, String defaultExchange) {
        StringBuilder builder = new StringBuilder();
        String exchange = null;
        String routingKey = "default";

        for (int i = 0; i < paramNames.length; ++i) {
            if (paramNames[i].equalsIgnoreCase("exchange")) {
                exchange = args[i].toString();
            } else if (paramNames[i].equalsIgnoreCase("routingKey")) {
                routingKey = args[i].toString();
            }
        }

        if (exchange == null || exchange.isEmpty()) {
            exchange = defaultExchange;
        }

        if (exchange == null || exchange.isEmpty()) {
            exchange = "default";
        }

        builder.append("exchange:").append(exchange).append("^routingKey:").append(routingKey);
        return builder.toString();
    }

    Map<String, Object> genReqParams(Object[] args, String[] paramNames) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> reqParams = new HashMap<>();
        for (int i = 0; i < args.length; ++i) {
            if (builder.length() > 0) {
                builder.append("^");
            }

            if (paramNames[i].equals("object")) {
                String s = "";
                Object obj = args[i];
                if (obj instanceof Message) {
                    Message m = (Message) obj;
                    s = AsyncAuditLog.bytesToTextAndEscape(m.getBody());
                } else {
                    s = AsyncAuditLog.toTextAndEscape(args[i]);
                }
                reqParams.put(paramNames[i], s);
            }
        }

        return reqParams;
    }

    Map genResParams(Throwable throwable) {
        Map map = new HashMap();
        if (throwable != null) {
            String s = AsyncAuditLog.escapeText(throwable.getMessage());
            map.put("exception", s);
            return map;
        } else {
            return map;
        }
    }
}
