package fun.pxyc.onelogger.db;

import java.sql.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbInstrument {
    private static final Logger log = LoggerFactory.getLogger(DbInstrument.class);
    static MySqlDriverInfo driverInfo = new MySqlDriver8();

    public DbInstrument() {}

    public static String getDatabase(Connection conn) {
        return driverInfo == null ? "unknown" : driverInfo.getDatabase(conn);
    }

    public static String getHostPort(Connection conn) {
        return driverInfo == null ? "0.0.0.0:0:0" : driverInfo.getHostPort(conn);
    }

    public static String parseDatabase(String url) {
        int p1 = url.indexOf("?");
        int p2;
        if (p1 >= 0) {
            p2 = url.lastIndexOf("/", p1);
            return url.substring(p2 + 1, p1);
        } else {
            p1 = url.indexOf("//");
            if (p1 >= 0) {
                p2 = url.indexOf("/", p1 + 2);
                if (p2 >= 0) {
                    return url.substring(p1 + 2, p2);
                } else {
                    p2 = url.indexOf("?", p1 + 2);
                    return p2 >= 0 ? url.substring(p1 + 2, p2) : url.substring(p1 + 2);
                }
            } else {
                return "unknown_db";
            }
        }
    }

    public static boolean mybatisExisted() {
        try {
            Class.forName("org.apache.ibatis.annotations.Select");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
