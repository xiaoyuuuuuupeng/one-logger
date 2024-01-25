package fun.pxyc.onelogger.log;

import fun.pxyc.onelogger.auditlog.AsyncAuditLog;
import fun.pxyc.onelogger.auditlog.AsyncAuditLogPool;
import fun.pxyc.onelogger.autoconfig.GlobalConfig;
import fun.pxyc.onelogger.utils.JsonUtil;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.dubbo.rpc.Invocation;
import org.slf4j.Logger;
import org.slf4j.MDC;

public class LoggerFormatter {

    private static LoggerFormatterHandler handler;
    private static List<LoggerFilter> loggerFilters = new ArrayList<>();

    protected static void Log(Logger logger, Map<String, Object> params) {
        boolean infoEnabled = logger.isInfoEnabled();
        if (!infoEnabled || handler == null) {
            return;
        }
        params.forEach((k, v) -> {
            if (k != null && v != null) {
                if ("throwable".equals(k)) {
                    MDC.put(k, "");
                }
                MDC.put(k, v.toString());
            }
        });
        for (LoggerFilter loggerFilter : loggerFilters) {
            params = loggerFilter.beforeBuildLogStr(params);
        }
        String logStr = handler.logStr(params);

        for (LoggerFilter loggerFilter : loggerFilters) {
            logStr = loggerFilter.afterBuildLogStr(logStr);
        }
        AsyncAuditLogPool.asyncLog(logger, logStr);
    }

    public static void setHandler(LoggerFormatterHandler handler) {
        LoggerFormatter.handler = handler;
    }

    public static void setFilters(List<LoggerFilter> filters) {
        if (filters != null) {
            LoggerFormatter.loggerFilters = filters;
        }
    }

    public static void addFilter(LoggerFilter filter) {
        if (LoggerFormatter.loggerFilters == null) {
            LoggerFormatter.loggerFilters = new ArrayList<>();
        }
        LoggerFormatter.loggerFilters.add(filter);
    }

    public static Map<String, Object> generateLogMap(
            String timestamp,
            String traceId,
            String spanId,
            String rootSpan,
            String connId,
            Action action,
            Integer sequence,
            String timeUsedMs,
            Map<String, Object> reqParams,
            Map<String, Object> resData,
            String exception,
            int logSize) {
        Map<String, Object> logMap = new LinkedHashMap<>();
        logMap.put("timestamp", timestamp);
        logMap.put("traceId", traceId);
        logMap.put("applicationName", GlobalConfig.globalServiceName);
        logMap.put("logType", action.getLogType());
        logMap.put("action", action.getAction());
        logMap.put("rootSpan", rootSpan);
        logMap.put("interface", action.getInterfaceName());
        logMap.put("method", action.getMethod());
        logMap.put("connId", connId);
        logMap.put("sourceIp", GlobalConfig.sourceIp);
        logMap.put("spanId", spanId);
        logMap.put("sequence", sequence);
        logMap.put("cost", getCost(timeUsedMs));
        logMap.put("request", reqParams);
        logMap.put("response", resData);
        if (exception != null && !exception.isEmpty()) {
            logMap.put("throwable", exception);
        }
        Map<String, String> map = MDC.getCopyOfContextMap();
        if (map != null && !map.isEmpty()) {
            for (String key : map.keySet()) {
                if (!logMap.containsKey(key) && !"TRACE_ID".equals(key) && !"extraInfo".equals(key)) {
                    logMap.put(key, map.get(key));
                }
            }
        }
        return logMap;
    }

    public static Long getCost(String timeUsedMs) {
        if (timeUsedMs.contains("ms")) {
            return Long.valueOf(timeUsedMs.split("ms")[0]);
        } else if (timeUsedMs.contains("ns")) {
            Long cost = Long.valueOf(timeUsedMs.split("ns")[0]);
            return cost / 1000L;
        }
        return 0L;
    }

    public String genException(Throwable throwable) {
        if (throwable != null) {
            return AsyncAuditLog.toTextAndEscape(throwable.getMessage(), -1);
        }
        return "";
    }

    public Map<String, Object> generateLogMapWithExtraInfo(
            String timestamp,
            String traceId,
            String spanId,
            String rootSpan,
            String connId,
            Action action,
            Integer sequence,
            String timeUsedMs,
            Map<String, Object> reqParams,
            Map<String, Object> resData,
            String exception,
            String extraInfo,
            int logSize) {

        Map<String, Object> logMap = generateLogMap(
                timestamp,
                traceId,
                spanId,
                rootSpan,
                connId,
                action,
                sequence,
                timeUsedMs,
                reqParams,
                resData,
                exception,
                logSize);
        logMap.put("extraInfo", extraInfo);
        return logMap;
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

    public Map<String, Object> getJsonReq(MethodInvocation joinPoint) {
        Map<String, Object> req = new LinkedHashMap<>();
        Object[] arguments = joinPoint.getArguments();
        Method method = joinPoint.getMethod();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < arguments.length; i++) {
            Parameter parameter = parameters[i];
            req.put(parameter.getName(), JsonUtil.toJsonObject(arguments[i]));
        }
        return req;
    }

    public Map<String, Object> getJsonRes(Object retObj) {
        Map<String, Object> res = new LinkedHashMap<>();
        if (retObj != null) {
            Map<String, Object> o = JsonUtil.toJsonMap(retObj);
            res.putAll(o);
        }
        return res;
    }

    public String getConnId() {
        return "0.0.0.0:0:0";
    }
}
