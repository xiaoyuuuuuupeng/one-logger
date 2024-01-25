package fun.pxyc.onelogger.db;

import java.sql.Connection;

public interface MySqlDriverInfo {
    String getDatabase(Connection connection);

    String getHostPort(Connection connection);
}
