package fun.pxyc.onelogger.trace;

import java.util.HashMap;
import java.util.Map;

public abstract class ContextData {
    public long requestTimeMicros;
    public long startMicros;
    public long timeUsedMicros;
    protected String connId;
    MetaData meta;
    Map<String, Object> attributes;
    int retCode;

    public ContextData(String connId, MetaData meta) {
        this.connId = connId;
        this.meta = meta;
    }

    public long elapsedMillisByNow() {
        return (System.nanoTime() / 1000L - this.startMicros) / 1000L;
    }

    public void end() {
        this.timeUsedMicros = System.nanoTime() / 1000L - this.startMicros;
    }

    public long getResponseTimeMicros() {
        return this.requestTimeMicros + this.timeUsedMicros;
    }

    public long getTimeUsedMicros() {
        return this.timeUsedMicros;
    }

    public void endWithTime(long timeUsedMicros) {
        this.timeUsedMicros = timeUsedMicros;
    }

    public long getTimeUsedMillis() {
        return this.timeUsedMicros / 1000L;
    }

    public String getClientIp() {
        String peers = this.meta.getTrace().getPeers();
        if (peers.isEmpty()) {
            return this.getRemoteIp();
        } else {
            String s = peers.split(",")[0];
            int p = s.indexOf(":");
            return p >= 0 ? s.substring(0, p) : s;
        }
    }

    public String getRemoteIp() {
        String remoteAddr = this.getRemoteAddr();
        int p = remoteAddr.lastIndexOf(":");
        return remoteAddr.substring(0, p);
    }

    public String getRemoteAddr() {
        int p = this.connId.lastIndexOf(":");
        return this.connId.substring(0, p);
    }

    public MetaData getMeta() {
        return this.meta;
    }

    public void setMeta(MetaData meta) {
        this.meta = meta;
    }

    public String getConnId() {
        return this.connId;
    }

    public void setConnId(String connId) {
        this.connId = connId;
    }

    public long getRequestTimeMicros() {
        return this.requestTimeMicros;
    }

    public long getStartMicros() {
        return this.startMicros;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        if (this.attributes == null) {
            this.attributes = new HashMap();
        }

        this.attributes = attributes;
    }

    public void setAttribute(String key, Object obj) {
        if (this.attributes == null) {
            this.attributes = new HashMap();
        }

        this.attributes.put(key, obj);
    }

    public Object getAttribute(String key) {
        return this.attributes == null ? null : this.attributes.get(key);
    }

    public void removeAttribute(String key) {
        if (this.attributes != null) {
            this.attributes.remove(key);
        }
    }

    public int getRetCode() {
        return this.retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }
}
