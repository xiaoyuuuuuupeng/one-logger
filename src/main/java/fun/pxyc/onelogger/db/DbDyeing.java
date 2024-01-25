package fun.pxyc.onelogger.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbDyeing {
    static ConcurrentHashMap<String, String> dyingedSqls = new ConcurrentHashMap();
    private static List<String> regexArray;
    private static String destValue;
    private static List<Pattern> loweredRegex;

    public DbDyeing() {}

    public static String undyeingTable(String tableName) {
        return tableName;
    }

    public static String dyeingSql(String sql) {
        String newSql = dyingedSqls.get(sql);
        if (newSql != null) {
            return newSql;
        } else {
            newSql = dyeingSqlInternal(sql);
            dyingedSqls.put(sql, newSql);
            return newSql;
        }
    }

    public static void init(String[] prefixArray) {
        regexArray = new ArrayList<String>();
        loweredRegex = new ArrayList<Pattern>();

        for (int i = 0; i < prefixArray.length; ++i) {
            String p = prefixArray[i];
            regexArray.add(genRegex(p.toLowerCase()));
            regexArray.add(genRegex(p.toUpperCase()));
            loweredRegex.add(Pattern.compile(genFindRegex(p.toLowerCase()), 32));
        }

        destValue = "$1$2_$3";
    }

    static String genRegex(String prefix) {
        return "([ \\.`])(" + prefix + "[a-zA-Z0-9_]+)([ \\.]*)";
    }

    static String genFindRegex(String prefix) {
        return ".*([ \\.`])(" + prefix + "[a-zA-Z0-9_]+).*";
    }

    public static String dyeingSqlInternal(String sql) {
        if (regexArray == null) {
            return sql;
        } else {
            String p;
            for (Iterator<String> iterator = regexArray.iterator();
                    iterator.hasNext();
                    sql = sql.replaceAll(p, destValue)) {
                p = iterator.next();
            }

            return sql;
        }
    }

    public static String findFirstTable(String sql0) {
        String sql = sql0.toLowerCase();
        if (loweredRegex == null) {
            return "unknown_table";
        } else {
            Iterator<Pattern> iterator = loweredRegex.iterator();
            Matcher m;
            do {
                if (!iterator.hasNext()) {
                    return "unknown_table";
                }
                Pattern p = iterator.next();
                m = p.matcher(sql);
            } while (!m.matches());

            return m.group(2);
        }
    }
}
