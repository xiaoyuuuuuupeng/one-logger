package fun.pxyc.onelogger.log;

import org.aspectj.lang.ProceedingJoinPoint;

public class Action {
    String logType;

    String interfaceName;

    String method;

    String action;

    public Action(String logType, String interfaceName, String method, String action) {
        this.logType = logType;
        this.interfaceName = interfaceName;
        this.method = method;
        this.action = action;
    }

    public Action(String logType, String action) {
        this.logType = logType;
        this.interfaceName = "";
        this.method = "";
        this.action = action;
    }

    public Action(String logType, String interfaceName, String method) {
        this.logType = logType;
        this.interfaceName = interfaceName;
        this.method = method;
        this.action = logType + ":" + interfaceName + "." + method;
    }

    public Action(String logType, ProceedingJoinPoint joinPoint) {
        this.logType = logType;
        this.interfaceName = joinPoint.getTarget().getClass().getSimpleName();
        this.method = joinPoint.getSignature().getName();
        this.action = logType + ":" + interfaceName + "." + method;
    }

    public String getLogType() {
        return this.logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getInterfaceName() {
        return this.interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
