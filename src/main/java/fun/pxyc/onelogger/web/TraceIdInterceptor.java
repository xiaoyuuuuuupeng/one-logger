package fun.pxyc.onelogger.web;

import fun.pxyc.onelogger.auditlog.AsyncAuditLog;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceData;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

public class TraceIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 生成web的traceId和span
        String remoteAddr = request.getRemoteAddr();
        TraceData t = AsyncAuditLog.newTrace(remoteAddr);
        Trace.setCurrentContext(Trace.getAdapter().newTraceContext(t, false));
        // 返回给客户端
        MDC.put("TRACE_ID", t.getTraceId());
        response.setHeader("TRACEID", t.getTraceId());
        return true;
    }
}
