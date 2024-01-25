package fun.pxyc.onelogger.http;

public class HttpClientProperties {
    private int connectTimeout = 3000;
    private int readTimeout = 3000;
    private boolean usePool = false;
    private int maxTotalConnect = 128;
    private int maxConnectPerRoute = 32;
    private int errorCountToAlarm = 5;
    private boolean logAllHeaders = false;
    private String logHeaderNames = "";

    public HttpClientProperties() {}

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getMaxTotalConnect() {
        return this.maxTotalConnect;
    }

    public void setMaxTotalConnect(int maxTotalConnect) {
        this.maxTotalConnect = maxTotalConnect;
    }

    public int getMaxConnectPerRoute() {
        return this.maxConnectPerRoute;
    }

    public void setMaxConnectPerRoute(int maxConnectPerRoute) {
        this.maxConnectPerRoute = maxConnectPerRoute;
    }

    public int getErrorCountToAlarm() {
        return this.errorCountToAlarm;
    }

    public void setErrorCountToAlarm(int errorCountToAlarm) {
        this.errorCountToAlarm = errorCountToAlarm;
    }

    public boolean isLogAllHeaders() {
        return this.logAllHeaders;
    }

    public void setLogAllHeaders(boolean logAllHeaders) {
        this.logAllHeaders = logAllHeaders;
    }

    public String getLogHeaderNames() {
        return this.logHeaderNames;
    }

    public void setLogHeaderNames(String logHeaderNames) {
        this.logHeaderNames = logHeaderNames;
    }

    public boolean isUsePool() {
        return this.usePool;
    }

    public void setUsePool(boolean usePool) {
        this.usePool = usePool;
    }
}
