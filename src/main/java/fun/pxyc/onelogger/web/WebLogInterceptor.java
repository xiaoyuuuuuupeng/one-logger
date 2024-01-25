package fun.pxyc.onelogger.web;

import fun.pxyc.onelogger.auditlog.AsyncAuditLog;
import fun.pxyc.onelogger.autoconfig.EnvConfigProps;
import fun.pxyc.onelogger.log.Action;
import fun.pxyc.onelogger.log.LoggerFormatter;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import fun.pxyc.onelogger.trace.TraceData;
import fun.pxyc.onelogger.utils.JsonUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

public class WebLogInterceptor extends LoggerFormatter implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(WebLogInterceptor.class);

    private final ConcurrentHashMap<String, AtomicInteger> seqMap = new ConcurrentHashMap<>();

    public static String getAddr() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        if (sra != null) {
            return "";
        }
        HttpServletRequest request = sra.getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getRemoteAddr();
        } else {
            ip = ip.split(",", 2)[0].trim();
        }
        return ip;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        TraceContext ctx = Trace.currentContext();
        if (ctx == null) {
            return methodInvocation.proceed();
        }
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        if (sra == null) {
            return methodInvocation.proceed();
        }
        HttpServletRequest request = sra.getRequest();
        HttpServletResponse response = sra.getResponse();
        if (response == null) {
            return methodInvocation.proceed();
        }
        String remoteAddr = request.getRemoteAddr();
        String action = "WEB." + request.getRequestURI();
        Span span = Trace.startForServer(ctx.getTrace(), "WEBSERVER", action);
        span.setRemoteAddr(remoteAddr);
        try {
            Object o = methodInvocation.proceed();
            span.stop(true);
            this.log(span, ctx, methodInvocation, o, null);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(span, ctx, methodInvocation, null, throwable);
            throw throwable;
        } finally {
            MDC.clear();
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
        TraceData trace = ctx.getTrace();
        long duration = span.getTimeUsedMicros();
        String costTime = span.getTimeUsedMs();
        String traceId = trace.getTraceId();
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String uri = request.getRequestURI();
        String remoteAddr = request.getRemoteAddr();
        if (duration >= EnvConfigProps.slowHttpMillis * 1000L) {
            log.warn("slow web request:" + uri + ", ts=" + duration + ", traceId=" + traceId);
        }
        String spanId = span.getSpanId();
        String serviceNameMsgName = "WEB." + uri;
        Action action = new Action(
                "WEB",
                joinPoint.getMethod().getDeclaringClass().getName(),
                joinPoint.getMethod().getName(),
                serviceNameMsgName);
        String timestamp = AsyncAuditLog.now(ctx, span);
        AtomicInteger atomicInteger = seqMap.get(uri);
        if (atomicInteger == null) {
            atomicInteger = new AtomicInteger(0);
            seqMap.put(uri, atomicInteger);
        }
        int sequence = atomicInteger.incrementAndGet();
        if (sequence >= 10000000) {
            atomicInteger.compareAndSet(sequence, 0);
        }
        Map<String, Object> extraMap = new LinkedHashMap<>();
        extraMap.put("clientIp", getClientIp(request));
        extraMap.put("clientPort", getPortInt(request));
        extraMap.put("header", getReqHeader(request));
        Log(
                AsyncAuditLog.webAuditLog,
                generateLogMapWithExtraInfo(
                        timestamp,
                        traceId,
                        spanId,
                        span.getRootSpan().getAction(),
                        remoteAddr,
                        action,
                        sequence,
                        costTime,
                        getJsonReq(request, joinPoint),
                        getJsonRes(retObj),
                        genException(throwable),
                        buildExtraInfo(extraMap),
                        EnvConfigProps.webLogMaxChars));
    }

    private String buildExtraInfo(Map extraMap) {
        StringBuilder b = new StringBuilder();
        if (extraMap == null) {
            return "";
        }
        extraMap.forEach((k, v) -> {
            b.append(k).append("=").append(v == null ? "" : v).append(";");
        });
        return b.toString();
    }

    public Map<String, Object> getJsonRes(Object retObj) {
        Map<String, Object> res = new LinkedHashMap<>();
        if (retObj != null) {
            Map<String, Object> map = JsonUtil.toJsonMap(retObj);
            if (map != null) {
                res.putAll(map);
            } else {
                res.put("res", retObj);
            }
        }
        return res;
    }

    private int getPortInt(HttpServletRequest request) {
        String port = request.getHeader("x-ff-port");
        return StringUtils.isEmpty(port) ? request.getRemotePort() : Integer.valueOf(port);
    }

    private String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("x-forwarded-for");
        if (StringUtils.isNotEmpty(clientIp) && clientIp.contains(",")) {
            clientIp = clientIp.substring(0, clientIp.indexOf(","));
        }
        clientIp = StringUtils.isNotEmpty(clientIp) ? clientIp : request.getRemoteAddr();
        return clientIp;
    }

    private void appendRes(StringBuilder b, Object retObj, Throwable throwable) {
        b.append("res").append(":").append(AsyncAuditLog.toTextAndEscape(retObj, EnvConfigProps.webLogMaxChars));
        if (throwable != null) {
            b.append("^exception").append(":").append(AsyncAuditLog.escapeText(throwable.getMessage(), -1));
        }
    }

    private Map<String, Object> getJsonReq(HttpServletRequest request, MethodInvocation joinPoint) {
        Map<String, Object> paramMap = paramMap(request);
        try {
            if (paramMap == null || paramMap.isEmpty()) {
                paramMap = new LinkedHashMap<>();
            }

            if (joinPoint.getArguments().length > 0) {
                Method signature = joinPoint.getMethod();
                // 参数注解，1维是参数的位置，2维是注解
                Annotation[][] annotations = signature.getParameterAnnotations();
                for (int i = 0; i < annotations.length; i++) {
                    Annotation[] paramAnn = annotations[i];
                    Object arg = joinPoint.getArguments()[i];
                    if (arg instanceof MultipartFile) {
                        MultipartFile fileRequest = (MultipartFile) arg;
                        arg = fileRequest.getOriginalFilename();
                    }
                    if (arg instanceof StandardMultipartHttpServletRequest) {
                        continue;
                    }
                    for (Annotation annotation : paramAnn) {
                        if (annotation.annotationType().equals(RequestBody.class)) {
                            // 取RequestBody注解所在位置的参数joinPoint.getArgs()[i]
                            paramMap.put("requestBody", arg);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return paramMap;
    }

    String getIp() {
        return getAddr();
    }

    public void appendReq(StringBuilder b, HttpServletRequest request, ProceedingJoinPoint joinPoint) {
        try {
            Map<String, Object> paramMap = paramMap(request);
            if (paramMap == null || paramMap.isEmpty()) {
                paramMap = new LinkedHashMap<>();
            }

            if (joinPoint.getArgs().length > 0) {
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                // 参数注解，1维是参数的位置，2维是注解
                Annotation[][] annotations = signature.getMethod().getParameterAnnotations();
                for (int i = 0; i < annotations.length; i++) {
                    Annotation[] paramAnn = annotations[i];
                    Object arg = joinPoint.getArgs()[i];
                    if (arg instanceof MultipartFile) {
                        MultipartFile fileRequest = (MultipartFile) arg;
                        arg = fileRequest.getOriginalFilename();
                    }
                    if (arg instanceof StandardMultipartHttpServletRequest) {
                        continue;
                    }
                    for (Annotation annotation : paramAnn) {
                        if (annotation.annotationType().equals(RequestBody.class)) {
                            // 取RequestBody注解所在位置的参数joinPoint.getArgs()[i]
                            paramMap.put("requestBody", arg);
                            break;
                        }
                    }
                }
            }
            if (paramMap.isEmpty()) {
                b.append("req is empty");
            } else {
                b.append("req:").append(JsonUtil.gsonParser(paramMap));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public Map<String, Object> paramMap(HttpServletRequest request) {
        Map<String, Object> map = new LinkedHashMap<>();

        Enumeration<String> paraNames = request.getParameterNames();

        while (paraNames.hasMoreElements()) {
            String paraName = paraNames.nextElement();
            map.put(paraName, request.getParameter(paraName));
        }

        return map;
    }

    private Map<String, Object> getReqHeader(HttpServletRequest request) {
        Map<String, Object> headerMap = new LinkedHashMap<>();
        if (EnvConfigProps.reqHeaders) {
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                headerMap.put(headerName, request.getHeader(headerName));
            }
        }
        return headerMap;
    }
}
