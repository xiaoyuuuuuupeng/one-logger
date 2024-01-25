package fun.pxyc.onelogger.trace;

public class TraceData {
    String peers; // ip:port for each hop, format: addr1,addr2,...
    String traceId;
    String parentSpanId; // may be empty
    String spanId;
    String tags; // key/value pairs passing in the chain, format: k1=v1&k2=v2&...
    int sampleFlag; //  0=default(yes) 1=force 2=no
    String dyeing;

    public TraceData() {}

    public String getPeers() {
        return peers;
    }

    public void setPeers(String peers) {
        this.peers = peers;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getParentSpanId() {
        return parentSpanId;
    }

    public void setParentSpanId(String parentSpanId) {
        this.parentSpanId = parentSpanId;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getSampleFlag() {
        return sampleFlag;
    }

    public void setSampleFlag(int sampleFlag) {
        this.sampleFlag = sampleFlag;
    }

    public String getDyeing() {
        return dyeing;
    }

    public void setDyeing(String dyeing) {
        this.dyeing = dyeing;
    }
}
