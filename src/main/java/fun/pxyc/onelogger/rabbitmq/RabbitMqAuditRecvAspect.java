package fun.pxyc.onelogger.rabbitmq;

import com.rabbitmq.client.Channel;
import fun.pxyc.onelogger.ServerContextData;
import fun.pxyc.onelogger.auditlog.AsyncAuditLog;
import fun.pxyc.onelogger.autoconfig.EnvConfigProps;
import fun.pxyc.onelogger.autoconfig.GlobalConfig;
import fun.pxyc.onelogger.log.Action;
import fun.pxyc.onelogger.log.LoggerFormatter;
import fun.pxyc.onelogger.trace.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;

@Aspect
public class RabbitMqAuditRecvAspect extends LoggerFormatter {
    private static final Logger log = LoggerFactory.getLogger(RabbitMqAuditRecvAspect.class);
    private final AtomicInteger seq = new AtomicInteger(0);

    public RabbitMqAuditRecvAspect() {}

    @Pointcut("execution(* *..ChannelAwareMessageListener.onMessage(..))")
    public void rabbitMqAuditRecvPointCut1() {}

    @Around("rabbitMqAuditRecvPointCut1()")
    public void rabbitMqAuditRecvAdvice1(ProceedingJoinPoint joinPoint) throws Throwable {
        this.doAdvice(joinPoint);
    }

    @Pointcut("@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
    public void rabbitMqAuditRecvPointCut2() {}

    @Around("rabbitMqAuditRecvPointCut2()")
    public void rabbitMqAuditRecvAdvice2(ProceedingJoinPoint joinPoint) throws Throwable {
        this.doAdvice(joinPoint);
    }

    public void doAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Message message = null;
        Channel channel = null;
        for (int i = 0; i < args.length; ++i) {
            Object arg = args[i];
            if (arg instanceof Channel) {
                channel = (Channel) arg;
            }
            if (arg instanceof Message) {
                message = (Message) arg;
            }
        }
        if (message != null) {
            String addr = null;
            if (channel != null) {
                addr = channel.getConnection().getAddress().getHostAddress() + ":"
                        + channel.getConnection().getPort();
            } else {
                addr = GlobalConfig.sourceIp;
            }
            TraceData t = AsyncAuditLog.newTrace(addr);
            String traceId = MDC.get("TRACE_ID");
            if (StringUtils.isNotEmpty(traceId)) {
                t.setTraceId(traceId);
            }
            Action action = new Action(
                    "RABBITMQ.RECEIVE",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName());
            Span span = Trace.startForServer(t, "RABBITMQ.RECEIVE", action.getAction());
            span.setRemoteAddr(addr);
            TraceContext ctx = Trace.currentContext();
            int sequence = this.seq.incrementAndGet();
            if (sequence >= 10000000) {
                this.seq.compareAndSet(sequence, 0);
            }
            String connId = addr + ":0";
            MetaData meta = new MetaData();
            meta.setSequence(sequence);
            meta.setDirection(0);
            meta.setTrace(t);
            ServerContextData sctx = new ServerContextData(connId, meta, ctx);
            ServerLogContextData.set(sctx);
            try {
                joinPoint.proceed();
                ctx.stopForServer(0);
                this.log(span, sctx, action, message, null);
            } catch (Throwable throwable) {
                ctx.stopForServer(-620);
                this.log(span, sctx, action, message, throwable);
                throw throwable;
            }
        } else {
            joinPoint.proceed();
        }
    }

    private void log(Span span, ServerContextData sctx, Action action, Message m, Throwable throwable) {
        try {
            this.logInternal(span, sctx, action, m, throwable);
        } catch (Throwable t) {
            log.error("logInternal exception", t);
        }
    }

    private void logInternal(Span span, ServerContextData sctx, Action action, Message m, Throwable throwable) {
        if (span == null) {
            log.error("span is null in RabbitMqAuditRecvAspect");
        } else {
            TraceContext ctx = sctx.getTraceContext();
            if (AsyncAuditLog.isMqRecvEnabled()) {
                TraceData trace = ctx.getTrace();
                String traceId = trace.getTraceId();
                String timestamp = AsyncAuditLog.now(ctx, span);
                String connId = sctx.getConnId();
                int sequence = sctx.getMeta().getSequence();
                String spanId = trace.getSpanId();
                Map reqParams = this.genReqParams(m);
                Map resParams = this.genResParams(throwable);
                Log(
                        AsyncAuditLog.mqRecvAuditLog,
                        generateLogMap(
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
                                EnvConfigProps.mqLogMaxChars));
            }
        }
    }

    public Map<String, Object> genReqParams(Message m) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("queue", m.getMessageProperties().getConsumerQueue());
        reqParams.put("deliverTag", m.getMessageProperties().getDeliveryTag());
        reqParams.put("body", AsyncAuditLog.bytesToTextAndEscape(m.getBody()));
        return reqParams;
    }

    public Map<String, Object> genResParams(Throwable throwable) {
        Map<String, Object> map = new HashMap<>();
        if (throwable != null) {
            String message = AsyncAuditLog.escapeText(throwable.getMessage());
            map.put("exception", message);
            return map;
        } else {
            return map;
        }
    }
}
