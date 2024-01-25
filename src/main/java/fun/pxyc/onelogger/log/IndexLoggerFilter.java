package fun.pxyc.onelogger.log;

import fun.pxyc.onelogger.utils.JsonUtil;
import java.util.List;
import java.util.Map;

public class IndexLoggerFilter implements LoggerFilter {

    private List<String> logIndex = null;

    @Override
    public Map<String, Object> beforeBuildLogStr(Map<String, Object> logParams) {
        // 处理索引提取
        if (logIndex != null && !logIndex.isEmpty()) {
            String json = JsonUtil.toJson(logParams);
            Map<String, Object> indexValue = JsonUtil.findJsonFieldValue(json, logIndex);
            if (!indexValue.isEmpty()) {
                logParams.putAll(indexValue);
            }
        }
        return logParams;
    }

    @Override
    public String afterBuildLogStr(String logStr) {
        return logStr;
    }

    public void setLogIndex(List<String> logIndex) {
        this.logIndex = logIndex;
    }
}
