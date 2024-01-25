package fun.pxyc.onelogger.trace;

public class Event {
    private final long startMicros = System.nanoTime() / 1000L;
    private String type;
    private String action;
    private String status;
    private String data;

    public Event(String type, String action, String status, String data) {
        this.type = type;
        this.action = action;
        this.status = status;
        this.data = data;
    }

    public String toAnnotationString() {
        String s = this.type + ":" + this.action + ":" + this.status;
        if (this.data != null) {
            s = s + ":" + this.data;
        }

        return s;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getStartMicros() {
        return this.startMicros;
    }
}
