package fun.pxyc.onelogger.log;

import com.jayway.jsonpath.JsonPath;
import fun.pxyc.onelogger.utils.JsonUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IgnoreLoggerFilter implements LoggerFilter {

    private static final String IGNORE_REPLACE_STR = "*****";
    private List<String> ignoreParams = new ArrayList<>();

    @Override
    public Map<String, Object> beforeBuildLogStr(Map<String, Object> logParams) {
        // 处理忽略字段问题
        if (ignoreParams != null && !ignoreParams.isEmpty()) {
            String json = JsonUtil.toJson(logParams);
            Map<String, Object> indexValue = JsonUtil.findJsonFieldValue(json, ignoreParams);
            if (indexValue != null && !indexValue.isEmpty()) {
                Set<String> keys = indexValue.keySet();
                for (String key : keys) {
                    json = JsonPath.parse(json).set(key, IGNORE_REPLACE_STR).jsonString();
                }
                logParams = JsonUtil.toMap(json);
            }
        }
        return logParams;
    }

    @Override
    public String afterBuildLogStr(String logStr) {
        return logStr;
    }

    public void setIgnoreParams(List<String> ignoreParams) {
        this.ignoreParams = ignoreParams;
    }
}
