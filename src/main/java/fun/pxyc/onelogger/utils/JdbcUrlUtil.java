package fun.pxyc.onelogger.utils;

import org.apache.commons.lang3.StringUtils;

public class JdbcUrlUtil {

    public static String findConnIdByUrl(String jdbcUrl) {
        int pos, pos1, pos2;
        String connUri;
        String host = "", port = "", database = "";
        if (jdbcUrl == null || !jdbcUrl.startsWith("jdbc:") || (pos1 = jdbcUrl.indexOf(':', 5)) == -1)
            throw new IllegalArgumentException("Invalid JDBC url.");

        if ((pos2 = jdbcUrl.indexOf(';', pos1)) == -1) {
            connUri = jdbcUrl.substring(pos1 + 1);
        } else {
            connUri = jdbcUrl.substring(pos1 + 1, pos2);
        }

        if (connUri.startsWith("//")) {
            if ((pos = connUri.indexOf('/', 2)) != -1) {
                host = connUri.substring(2, pos);
                database = connUri.substring(pos + 1);

                if ((pos = host.indexOf(':')) != -1) {
                    port = host.substring(pos + 1);
                    host = host.substring(0, pos);
                }
            }
        } else {
            database = connUri;
        }
        return "hosts:" + host + "^_^port:" + port + "^_^database:" + database;
    }

    public static String findDataBaseNameByUrl(String jdbcUrl) {
        String database = null;
        int pos, pos1;
        String connUri;

        if (StringUtils.isBlank(jdbcUrl)) {
            return "";
        }

        jdbcUrl = jdbcUrl.toLowerCase();

        if (jdbcUrl.startsWith("jdbc:impala")) {
            jdbcUrl = jdbcUrl.replace(":impala", "");
        }

        if (!jdbcUrl.startsWith("jdbc:") || (pos1 = jdbcUrl.indexOf(':', 5)) == -1) {
            throw new IllegalArgumentException("Invalid JDBC url.");
        }

        connUri = jdbcUrl.substring(pos1 + 1);

        if (connUri.startsWith("//")) {
            if ((pos = connUri.indexOf('/', 2)) != -1) {
                database = connUri.substring(pos + 1);
            }
        } else {
            database = connUri;
        }

        if (StringUtils.isBlank(database)) {
            return "";
        }

        if (database.contains("?")) {
            database = database.substring(0, database.indexOf("?"));
        }

        if (database.contains(";")) {
            database = database.substring(0, database.indexOf(";"));
        }
        return database;
    }
}
