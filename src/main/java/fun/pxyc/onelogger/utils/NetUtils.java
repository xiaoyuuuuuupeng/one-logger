package fun.pxyc.onelogger.utils;

import java.net.InetAddress;

public class NetUtils {
    public static String getHostAddress() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            return ia.getHostAddress();
        } catch (Exception e) {
            return "0.0.0.0";
        }
    }
}
