package fun.pxyc.onelogger.db;

public class DbMsgNameHolder {
    public static final ThreadLocal<String> msgName = new ThreadLocal();

    public DbMsgNameHolder() {}
}
