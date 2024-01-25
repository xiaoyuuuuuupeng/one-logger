package fun.pxyc.onelogger.log;

import java.util.Map;

public interface LoggerFilter {

    Map<String, Object> beforeBuildLogStr(Map<String, Object> logParams);

    String afterBuildLogStr(String logStr);
}
