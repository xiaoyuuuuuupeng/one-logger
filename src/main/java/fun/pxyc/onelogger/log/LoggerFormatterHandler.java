package fun.pxyc.onelogger.log;

import java.util.Map;

public interface LoggerFormatterHandler {

    String logStr(Map<String, Object> params);
}
