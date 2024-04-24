package fun.pxyc.onelogger.dubbo;

import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;

import fun.pxyc.onelogger.auditlog.AsyncAuditLog;
import fun.pxyc.onelogger.autoconfig.EnvConfigProps;
import fun.pxyc.onelogger.log.Action;
import fun.pxyc.onelogger.log.LoggerFormatter;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import fun.pxyc.onelogger.trace.TraceData;
import fun.pxyc.onelogger.utils.JsonUtil;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Activate(
        group = {PROVIDER},
        order = -9999)
public class ProviderTraceFilter extends LoggerFormatter implements Filter {
    public static final String TRACE_ID = "traceId";
    public static final String CLIENT_IP = "clientIp";
    private static final Logger log = LoggerFactory.getLogger(ProviderTraceFilter.class);
    private final ConcurrentHashMap<String, AtomicInteger> seqMap = new ConcurrentHashMap<>();

    public static String getAddr() {
        String addr = RpcContext.getContext().getAttachment(CLIENT_IP);
        if (addr == null || addr.isEmpty()) {
            addr = RpcContext.getContext().getRemoteHost() + ":"
                    + RpcContext.getContext().getRemotePort();
        }
        return addr;
    }

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        String addr = getAddr();
        TraceData t = AsyncAuditLog.newTrace(addr);
        String traceId = RpcContext.getContext().getAttachment(TRACE_ID);
        if (traceId != null && !traceId.isEmpty()) {
            t.setTraceId(traceId);
            MDC.put("TRACE_ID", traceId);
        }
        String remoteInterface = invocation.getInvoker().getInterface().getName();
        String remoteMethod = invocation.getMethodName();
        String action = remoteInterface + "_" + remoteMethod;
        Span span = Trace.startForServer(t, "DUBBOPROVIDER", action);
        span.setRemoteAddr(addr);
        TraceContext ctx = Trace.currentContext();
        Result o = null;
        try {
            o = invoker.invoke(invocation);
            span.stop(true);
            this.log(span, ctx, invocation, o, null);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(span, ctx, invocation, null, throwable);
            throw throwable;
        }
    }

    private void log(Span span, TraceContext context, Invocation invocation, Result result, Throwable throwable) {
        try {
            this.logInternal(span, context, invocation, result, throwable);
        } catch (Throwable t) {
            log.error("logInternal exception", t);
            log.error(t.getMessage(), t);
        }
    }

    private void logInternal(Span span, TraceContext ctx, Invocation invocation, Result result, Throwable throwable) {
        TraceData trace = ctx.getTrace();
        long duration = span.getTimeUsedMicros();
        String costTime = span.getTimeUsedMs();

        String traceId = trace.getTraceId();
        String remoteInterface = invocation.getInvoker().getInterface().getName();
        String remoteMethod = invocation.getMethodName();
        String dubboUrl = remoteInterface + "_" + remoteMethod;
        // span.changeAction("DUBBO." + dubboUrl);
        if (duration >= EnvConfigProps.slowDubboMillis * 1000L) {
            log.warn("slow dubbo request:" + dubboUrl + ", ts=" + duration + ", traceId=" + traceId);
        }

        if (AsyncAuditLog.isDubboProviderAuditLogEnabled()) {
            String timestamp = AsyncAuditLog.now(ctx, span);
            AtomicInteger atomicInteger = seqMap.get(dubboUrl);
            if (atomicInteger == null) {
                atomicInteger = new AtomicInteger(0);
                seqMap.put(dubboUrl, atomicInteger);
            }
            int sequence = atomicInteger.incrementAndGet();
            if (sequence >= 10000000) {
                atomicInteger.compareAndSet(sequence, 0);
            }
            String spanId = span.getSpanId();
            String serviceNameMsgName = "DUBBO." + dubboUrl;
            Log(
                    AsyncAuditLog.dubboProviderAuditLog,
                    generateLogMap(
                            timestamp,
                            traceId,
                            spanId,
                            span.getRootSpan().getAction(),
                            getAddr(),
                            new Action("DUBBO", remoteInterface, remoteMethod, serviceNameMsgName),
                            sequence,
                            costTime,
                            getJsonReq(invocation),
                            getJsonRes(result),
                            genException(throwable),
                            EnvConfigProps.dubboLogMaxChars));
        }
    }

    public Map<String, Object> getJsonReq(Invocation invocation) {
        Object[] arguments = invocation.getArguments();
        Map<String, Object> argMap = new LinkedHashMap<>();
        Class<?>[] parameterTypes = invocation.getParameterTypes();
        if (arguments == null || arguments.length == 0) {
            return argMap;
        }
        for (int i = 0; i < arguments.length; i++) {
            String simpleName = parameterTypes.getClass().getSimpleName();
            argMap.put(simpleName.toLowerCase(), JsonUtil.toJsonObject(arguments[i]));
        }
        return argMap;
    }
}
