package fun.pxyc.onelogger.redis;

import fun.pxyc.onelogger.auditlog.AsyncAuditLog;
import fun.pxyc.onelogger.autoconfig.EnvConfigProps;
import fun.pxyc.onelogger.redis.proxy.*;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import fun.pxyc.onelogger.trace.TraceData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.*;

@Aspect
public class RedisAuditLogAspect {
    private static final Logger log = LoggerFactory.getLogger(RedisAuditLogAspect.class);

    public RedisAuditLogAspect() {}

    @Pointcut("execution(* org.springframework.data.redis.core.RedisTemplate.opsForValue(..))")
    public void opsForValuePointCut() {}

    @Around("opsForValuePointCut()")
    public Object opsForValueAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object object = joinPoint.proceed();
        return new ValueOperationsProxy((ValueOperations) object);
    }

    @Pointcut("execution(* org.springframework.data.redis.core.RedisTemplate.opsForList(..))")
    public void opsForListPointCut() {}

    @Around("opsForListPointCut()")
    public Object opsForListAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        Object object = joinPoint.proceed();
        return new ListOperationsProxy((ListOperations) object);
    }

    @Pointcut("execution(* org.springframework.data.redis.core.RedisTemplate.opsForHash(..))")
    public void opsForHashPointCut() {}

    @Around("opsForHashPointCut()")
    public Object opsForHashAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        Object object = joinPoint.proceed();
        return new HashOperationsProxy((HashOperations) object);
    }

    @Pointcut("execution(* org.springframework.data.redis.core.RedisTemplate.opsForSet(..))")
    public void opsForSetPointCut() {}

    @Around("opsForSetPointCut()")
    public Object opsForSetAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        Object object = joinPoint.proceed();
        return new SetOperationsProxy((SetOperations) object);
    }

    @Pointcut("execution(* org.springframework.data.redis.core.RedisTemplate.opsForZSet(..))")
    public void opsForZSetPointCut() {}

    @Around("opsForZSetPointCut()")
    public Object opsForZSetAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        Object object = joinPoint.proceed();
        return new ZSetOperationsProxy((ZSetOperations) object);
    }

    @Pointcut("execution(* org.springframework.data.redis.core.RedisTemplate.opsForGeo(..))")
    public void opsForGeoPointCut() {}

    @Around("opsForGeoPointCut()")
    public Object opsForGeoAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        Object object = joinPoint.proceed();
        return new GeoOperationsProxy((GeoOperations) object);
    }

    @Pointcut("execution(* org.springframework.data.redis.core.RedisTemplate.boundValueOps(..))")
    public void boundValueOpsPointCut() {}

    @Around("boundValueOpsPointCut()")
    public Object boundValueOpsAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        RedisTemplate t = (RedisTemplate) joinPoint.getTarget();

        Object[] args = joinPoint.getArgs();
        return new BoundValueOperationsProxy(t.boundValueOps(args[0]));
    }

    @Pointcut("execution(* org.springframework.data.redis.core.RedisTemplate.boundListOps(..))")
    public void boundListOpsPointCut() {}

    @Around("boundListOpsPointCut()")
    public Object boundListOpsAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        RedisTemplate t = (RedisTemplate) joinPoint.getTarget();

        Object[] args = joinPoint.getArgs();
        return new BoundListOperationsProxy(t.boundListOps(args[0]));
    }

    @Pointcut("execution(* org.springframework.data.redis.core.RedisTemplate.boundHashOps(..))")
    public void boundHashOpsPointCut() {}

    @Around("boundHashOpsPointCut()")
    public Object boundHashOpsAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        RedisTemplate t = (RedisTemplate) joinPoint.getTarget();

        Object[] args = joinPoint.getArgs();

        return new BoundHashOperationsProxy(t.boundHashOps(args[0]));
    }

    @Pointcut("execution(* org.springframework.data.redis.core.RedisTemplate.boundSetOps(..))")
    public void boundSetOpsPointCut() {}

    @Around("boundSetOpsPointCut()")
    public Object boundSetOpsAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        RedisTemplate t = (RedisTemplate) joinPoint.getTarget();

        Object[] args = joinPoint.getArgs();
        return new BoundSetOperationsProxy(t.boundSetOps(args[0]));
    }

    @Pointcut("execution(* org.springframework.data.redis.core.RedisTemplate.boundZSetOps(..))")
    public void boundZSetOpsPointCut() {}

    @Around("boundZSetOpsPointCut()")
    public Object boundZSetOpsAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        RedisTemplate t = (RedisTemplate) joinPoint.getTarget();

        Object[] args = joinPoint.getArgs();

        return new BoundZSetOperationsProxy(t.boundZSetOps(args[0]));
    }

    @Pointcut(
            "execution(* org.springframework.data.redis.core.RedisTemplate.delete(..))||execution(* org.springframework.data.redis.core.RedisTemplate.rename(..))||execution(* org.springframework.data.redis.core.RedisTemplate.expire(..)) ||execution(* org.springframework.data.redis.core.RedisTemplate.expireAt(..)) ||execution(* org.springframework.data.redis.core.RedisTemplate.hasKey(..)) ||execution(* org.springframework.data.redis.core.RedisTemplate.getExpire(..)) ||execution(* org.springframework.data.redis.core.RedisTemplate.keys(..))")
    public void appointPointCut() {}

    @Around("appointPointCut()")
    public Object appointAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        Span span = Trace.startAsync("REDIS", methodName.toLowerCase());
        TraceContext ctx = Trace.currentContext();
        Object o = null;

        try {
            o = joinPoint.proceed();
            span.stop(true);
            this.log(o, span, ctx, joinPoint, null);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, ctx, joinPoint, throwable);
            throw throwable;
        }
    }

    private void log(Object res, Span span, TraceContext ctx, ProceedingJoinPoint joinPoint, Throwable throwable) {
        try {
            this.logInternal(res, span, ctx, joinPoint, throwable);
        } catch (Throwable t) {
            log.error("logInternal exception", t);
        }
    }

    private void logInternal(
            Object res, Span span, TraceContext ctx, ProceedingJoinPoint joinPoint, Throwable throwable) {
        Object[] args = joinPoint.getArgs();
        TraceData trace = ctx.getTrace();
        String traceId = trace.getTraceId();
        String methodName = joinPoint.getSignature().getName().toLowerCase();
        long duration = span.getTimeUsedMicros();
        if (duration >= EnvConfigProps.slowRedisMillis * 1000L) {
            log.warn("slow redis: ts=" + duration + ", traceId=" + traceId);
        }

        if (AsyncAuditLog.isRedisEnabled()) {
            RedisLogFormat.log(res, span, methodName, ctx, throwable, args);
        }
    }
}
