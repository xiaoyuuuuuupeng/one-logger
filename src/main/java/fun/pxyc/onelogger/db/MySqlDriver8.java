package fun.pxyc.onelogger.db;

import com.mysql.cj.jdbc.ConnectionImpl;
import java.lang.reflect.Field;
import java.sql.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySqlDriver8 implements MySqlDriverInfo {
    private static final Logger log = LoggerFactory.getLogger(MySqlDriver8.class);
    static Field databaseField;

    static {
        initFields();
    }

    public MySqlDriver8() {}

    static void initFields() {
        try {
            databaseField = ConnectionImpl.class.getDeclaredField("database");
            databaseField.setAccessible(true);
        } catch (Throwable throwable) {
            log.error("cannot update JDBC4Connection database field");
            databaseField = null;
        }
    }

    public String getDatabase(Connection conn) {
        if (!(conn instanceof ConnectionImpl)) {
            return "unknown";
        } else if (databaseField == null) {
            return "unknown";
        } else {
            try {
                String s = (String) databaseField.get(conn);
                return s != null && !s.isEmpty() ? s : "unknown";
            } catch (Throwable throwable) {
                log.error("cannot get JDBC4Connection database field");
                return "unknown";
            }
        }
    }

    public String getHostPort(Connection conn) {
        if (conn instanceof ConnectionImpl) {
            ConnectionImpl mysqlConn = (ConnectionImpl) conn;
            return mysqlConn.getHostPortPair() + ":" + mysqlConn.getId();
        } else {
            return "0.0.0.0:0:0";
        }
    }
}
