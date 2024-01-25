package fun.pxyc.onelogger.log;

import fun.pxyc.onelogger.auditlog.AsyncAuditLog;
import fun.pxyc.onelogger.utils.JsonUtil;
import java.util.Map;
import java.util.Set;

public class PlainLoggerFormatterHandler implements LoggerFormatterHandler {
    @Override
    public String logStr(Map<String, Object> params) {
        StringBuilder stringBuilder = new StringBuilder();
        Set<Map.Entry<String, Object>> entries = params.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            Object v = entry.getValue();
            if (v instanceof Map) {
                Map<String, Object> map = JsonUtil.toJsonMap(v);
                stringBuilder.append(AsyncAuditLog.mapToString(map)).append(",   ");
                continue;
            }
            stringBuilder.append(AsyncAuditLog.toTextAndEscape(v, -1)).append(",   ");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 4);
    }
}
