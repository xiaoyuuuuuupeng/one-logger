package fun.pxyc.onelogger.log;

import fun.pxyc.onelogger.utils.JsonUtil;
import java.util.Map;

public class JsonLoggerFormatterHandler implements LoggerFormatterHandler {

    @Override
    public String logStr(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        return JsonUtil.toJson(params);
    }
}
