package fun.pxyc.onelogger.trace;

import fun.pxyc.onelogger.ServerContextData;

public class ServerLogContextData {
    static ThreadLocal<ServerContextData> tlData = new ThreadLocal();

    public ServerLogContextData() {}

    public static ServerContextData get() {
        return tlData.get();
    }

    public static void set(ServerContextData data) {
        tlData.set(data);
        Trace.restoreContext(data.getTraceContext());
    }

    public static void remove() {
        tlData.remove();
    }

    public static void logVar(String key, Object value) {
        ServerContextData data = tlData.get();
        if (data != null) {
            data.setAttribute("var:" + key, value);
        }
    }
}
