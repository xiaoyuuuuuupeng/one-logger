package fun.pxyc.onelogger.kafka;

import fun.pxyc.onelogger.ServerContextData;
import fun.pxyc.onelogger.auditlog.AsyncAuditLog;
import fun.pxyc.onelogger.autoconfig.EnvConfigProps;
import fun.pxyc.onelogger.log.Action;
import fun.pxyc.onelogger.log.LoggerFormatter;
import fun.pxyc.onelogger.trace.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class KafkaAuditLogListenerAspect extends LoggerFormatter {
    private static final Logger log = LoggerFactory.getLogger(KafkaAuditLogListenerAspect.class);
    private final AtomicInteger seq = new AtomicInteger(0);

    public KafkaAuditLogListenerAspect() {}

    @Pointcut("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public void kafkaAuditLogListenerPointCut() {}

    @Around("kafkaAuditLogListenerPointCut()")
    public void kafkaAuditLogListenerAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        this.doAdvice(joinPoint);
    }

    public void doAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Consumer<?, ?> consumer = null;
        ConsumerRecord<?, ?> m = null;
        List<?> mlist = null;
        int length = args.length;
        for (int i = 0; i < length; ++i) {
            Object arg = args[i];
            if (arg instanceof Consumer) {
                consumer = (Consumer<?, ?>) arg;
            }

            if (arg instanceof ConsumerRecord) {
                m = (ConsumerRecord<?, ?>) arg;
            }

            if (arg instanceof List) {
                List<?> list = (List<?>) arg;
                if (list.size() > 0 && list.get(0) instanceof ConsumerRecord) {
                    mlist = list;
                }
            }
        }

        if ((m != null || mlist != null) && consumer != null) {
            String address = this.getAddress(consumer);
            TraceData t = AsyncAuditLog.newTrace(address);
            String interfaceName =
                    joinPoint.getTarget().getClass().getSimpleName().toLowerCase() + "."
                            + joinPoint.getSignature().getName().toLowerCase();
            String serviceNameMsgName = "KAFKA." + "RECEIVE";
            Action action = new Action(
                    "KAFKA.RECEIVE",
                    joinPoint.getTarget().getClass().getName(),
                    joinPoint.getSignature().getName(),
                    serviceNameMsgName);
            Span span = Trace.startForServer(t, "KAFKASERVER", interfaceName);
            span.setRemoteAddr(address);
            TraceContext ctx = Trace.currentContext();
            int sequence = this.seq.incrementAndGet();
            if (sequence >= 10000000) {
                this.seq.compareAndSet(sequence, 0);
            }

            String connId = address + ":0";
            MetaData meta = new MetaData();
            meta.setSequence(sequence);
            meta.setDirection(1);
            meta.setTrace(t);
            ServerContextData sctx = new ServerContextData(connId, meta, ctx);
            ServerLogContextData.set(sctx);
            try {
                joinPoint.proceed();
                ctx.stopForServer(0);
                if (m != null) {
                    this.log(span, sctx, action, m, null);
                } else {
                    this.log(span, sctx, action, mlist, null);
                }

            } catch (Throwable throwable) {
                ctx.stopForServer(-1);
                if (m != null) {
                    this.log(span, sctx, action, m, throwable);
                } else {
                    this.log(span, sctx, action, mlist, throwable);
                }
                throw throwable;
            }
        } else {
            joinPoint.proceed();
        }
    }

    String getAddress(Consumer<?, ?> consumer) {
        return KafkaInstrument.getIpPort(consumer);
    }

    private void log(Span span, ServerContextData sctx, Action action, ConsumerRecord<?, ?> m, Throwable throwable) {
        try {
            this.logInternal(span, sctx, action, m, throwable);
        } catch (Throwable t) {
            log.error("logInternal exception", t);
        }
    }

    private void log(Span span, ServerContextData sctx, Action action, List<?> mlist, Throwable throwable) {
        Iterator<?> m = mlist.iterator();
        while (m.hasNext()) {
            ConsumerRecord<?, ?> mr = (ConsumerRecord<?, ?>) m.next();
            try {
                this.logInternal(span, sctx, action, mr, throwable);
            } catch (Throwable t) {
                log.error("logInternal exception", t);
            }
        }
    }

    private void logInternal(
            Span span, ServerContextData sctx, Action action, ConsumerRecord<?, ?> m, Throwable throwable) {
        if (span == null) {
            log.error("span is null in KafkaAuditLogListenerAspect");
        } else {
            TraceContext ctx = sctx.getTraceContext();
            MetaData meta = sctx.getMeta();
            TraceData trace = ctx.getTrace();
            String timestamp = AsyncAuditLog.now(ctx, span);
            if (AsyncAuditLog.isKafkaRecvEnabled()) {
                LoggerFormatter.Log(
                        AsyncAuditLog.kafkaRecvAuditLog,
                        generateLogMap(
                                timestamp,
                                trace.getTraceId(),
                                trace.getSpanId(),
                                span.getRootSpan().getAction(),
                                sctx.getConnId(),
                                action,
                                meta.getSequence(),
                                span.getTimeUsedMs(),
                                genReqParams(m),
                                genResParams(throwable),
                                genException(throwable),
                                EnvConfigProps.kafkaLogMaxChars));
            }
        }
    }

    Map<String, Object> genReqParams(ConsumerRecord<?, ?> m) {
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("partition", m.partition());
        reqMap.put("topic", m.topic());
        reqMap.put("offset", m.offset());
        reqMap.put("body", m.value());
        return reqMap;
    }

    private Map<String, Object> genResParams(Throwable throwable) {
        Map<String, Object> resMap = new HashMap<>();
        if (throwable != null) {
            String exceptionStr = AsyncAuditLog.escapeText(throwable.getMessage());
            resMap.put("exception", exceptionStr);
        }
        return resMap;
    }
}
