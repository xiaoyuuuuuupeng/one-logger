package fun.pxyc.onelogger.trace;

public class MetaData {
    TraceData trace;
    private Integer direction = 0;
    private Integer serviceId = 0;
    private Integer msgId = 0;
    private Integer sequence = 0;
    private Integer timeout = 0;
    private Integer retCode = 0;
    private String attachment = "";
    private Integer compress = 0;
    private Integer encrypt = 0;

    public MetaData() {}

    public MetaData(
            Integer direction,
            Integer serviceId,
            Integer msgId,
            Integer sequence,
            Integer timeout,
            Integer retCode,
            String attachment,
            Integer compress,
            Integer encrypt) {
        this.direction = direction;
        this.serviceId = serviceId;
        this.msgId = msgId;
        this.sequence = sequence;
        this.timeout = timeout;
        this.retCode = retCode;
        this.attachment = attachment;
        this.compress = compress;
        this.encrypt = encrypt;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getRetCode() {
        return retCode;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Integer getCompress() {
        return compress;
    }

    public void setCompress(Integer compress) {
        this.compress = compress;
    }

    public Integer getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(Integer encrypt) {
        this.encrypt = encrypt;
    }

    public TraceData getTrace() {
        return trace;
    }

    public void setTrace(TraceData trace) {
        this.trace = trace;
    }
}
