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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LoggerFormatter {

    private static LoggerFormatterHandler handler;
    private static List<LoggerFilter> loggerFilters = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(LoggerFormatter.class);

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
        String reqStr = reqParams != null ? JsonUtil.toJson(reqParams) : "";
        if (reqStr == null) {
            logger.error("generate reqJsonStr error,reqParams is {}", JsonUtil.toJson(reqParams));
            reqStr = "";
        }
        if (logSize != -1 && logSize != 0) {
            if (reqStr.length() > logSize - 3) {
                reqStr = reqStr.substring(0, logSize - 3);
                reqStr = reqStr + "...";
            }
        }
        String resStr = resData != null ? JsonUtil.toJson(resData) : "";
        if (resStr == null) {
            logger.error("generate reqJsonStr error,reqParams is {}", JsonUtil.toJson(reqParams));
            resStr = "";
        }
        if (logSize != -1 && logSize != 0) {
            if (resStr.length() > logSize - 3) {
                resStr = resStr.substring(0, logSize - 3);
                resStr = resStr + "...";
            }
        }
        logMap.put("reqStr", reqStr);
        logMap.put("resStr", resStr);
        if (exception != null && !exception.isEmpty()) {
            logMap.put("throwable", exception);
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
