package fun.pxyc.onelogger.http;

import java.net.URI;
import java.util.Map;

public class HttpCallInfo {
    public static ThreadLocal<HttpCallInfo> instance = new ThreadLocal<>();
    public URI uri;
    public String method;
    public Map<String, String> reqHeaders;
    public String reqBodyStr;
    public int httpCode;
    public Map<String, String> resHeaders;
    public String resBodyStr;

    public HttpCallInfo() {}
}
