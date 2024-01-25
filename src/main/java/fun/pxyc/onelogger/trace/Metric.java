package fun.pxyc.onelogger.trace;

public class Metric {
    public static final int COUNT = 1;
    public static final int QUANTITY = 2;
    public static final int SUM = 3;
    public static final int QUANTITY_AND_SUM = 4;
    private String key;
    private int type;
    private String value;

    public Metric(String key, int type, String value) {
        this.key = key;
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
