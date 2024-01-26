package fun.pxyc.onelogger.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import fun.pxyc.onelogger.auditlog.AsyncAuditLog;
import fun.pxyc.onelogger.autoconfig.EnvConfigProps;
import fun.pxyc.onelogger.db.DbDyeing;
import fun.pxyc.onelogger.db.DbMsgNameHolder;
import fun.pxyc.onelogger.log.Action;
import fun.pxyc.onelogger.log.LoggerFormatter;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import fun.pxyc.onelogger.trace.TraceData;
import fun.pxyc.onelogger.utils.JdbcUrlUtil;
import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.sql.DataSource;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({
    @Signature(
            type = Executor.class,
            method = "update",
            args = {MappedStatement.class, Object.class}), // 需要代理的对象和方法
    @Signature(
            type = Executor.class,
            method = "query",
            args = {
                MappedStatement.class,
                Object.class,
                RowBounds.class,
                ResultHandler.class,
                CacheKey.class,
                BoundSql.class
            }),
    @Signature(
            type = Executor.class,
            method = "query",
            args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}) // 需要代理的对象和方法
})
public class SqlStatsInterceptor extends LoggerFormatter implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger(SqlStatsInterceptor.class);

    private final AtomicInteger seq = new AtomicInteger(0);

    /**
     * 如果是字符串对象则加上单引号返回，如果是日期则也需要转换成字符串形式，如果是其他则直接转换成字符串返回。
     *
     * @param obj 参数对象
     * @return 返回parameterObject的值
     */
    private static String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'" + obj + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(obj) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

    /**
     * 生成对应的带有值得sql语句
     *
     * @param configuration
     * @param boundSql
     * @return
     */
    public static String geneSql(Configuration configuration, BoundSql boundSql) {

        Object parameterObject = boundSql.getParameterObject(); // 获得参数对象，如{id:1,name:"user1",param1:1,param2:"user1"}
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings(); // 获得映射的对象参数
        String sql = boundSql.getSql().replaceAll("[\\s]+", " "); // 获得带问号的sql语句
        if (parameterMappings.size() > 0 && parameterObject != null) { // 如果参数个数大于0且参数对象不为空，说明该sql语句是带有条件的
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) { // 检查该参数是否是一个参数
                // getParameterValue用于返回是否带有单引号的字符串，如果是字符串则加上单引号
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject)); // 如果是一个参数则只替换一次，将问号直接替换成值
            } else {
                MetaObject metaObject =
                        configuration.newMetaObject(parameterObject); // 将映射文件的参数和对应的值返回，比如：id，name以及对应的值。
                for (ParameterMapping parameterMapping : parameterMappings) { // 遍历参数，如:id,name等
                    String propertyName = parameterMapping.getProperty(); // 获得属性名，如id,name等字符串
                    if (metaObject.hasGetter(propertyName)) { // 检查该属性是否在metaObject中
                        Object obj = metaObject.getValue(propertyName); // 如果在metaObject中，那么直接获取对应的值
                        sql = sql.replaceFirst("\\?", getParameterValue(obj)); // 然后将问号?替换成对应的值。
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        return sql; // 最后将sql语句返回
    }

    public static String parseAction(String sql) {
        int p = sql.indexOf(" ");
        String action = p >= 0 ? sql.substring(0, p) : sql;
        if (action.length() > 7) {
            action = action.substring(0, 7);
        }
        return action.toLowerCase();
    }

    public static String parseMsgName(String sql, String oldMsgName) {
        String action = parseAction(sql);
        if (oldMsgName != null && oldMsgName.contains("batch")) {
            action = "batch" + action;
        }

        String tableName = getTableName(sql);
        return tableName + ".jdbc_" + action;
    }

    public static String getTableName(String sql) {
        return DbDyeing.undyeingTable(DbDyeing.findFirstTable(sql));
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Span span = Trace.startAsync("DB", "TRANS");
        Object proceed = null;
        try {
            proceed = invocation.proceed();
            span.stop(true);
            this.log(span, Trace.currentContext(), invocation, proceed, null);
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(span, Trace.currentContext(), invocation, null, throwable);
            throw throwable;
        }
        return proceed;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    private void log(Span span, TraceContext context, Invocation invocation, Object result, Throwable throwable) {
        try {
            this.logInternal(span, context, invocation, result, throwable);
        } catch (Throwable t) {
            log.error("logInternal exception", t);
            log.error(t.getMessage(), t);
        }
    }

    private void logInternal(Span span, TraceContext ctx, Invocation invocation, Object result, Throwable throwable)
            throws Throwable {
        TraceData trace = ctx.getTrace();
        int sequence = this.seq.incrementAndGet();
        if (sequence >= 10000000) {
            this.seq.compareAndSet(sequence, 0);
        }
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        String sqlId = mappedStatement.getId();
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        String sql = geneSql(configuration, boundSql);
        String msgName = DbMsgNameHolder.msgName.get();
        if (msgName == null || msgName.startsWith("jdbc_")) {
            msgName = parseMsgName(sql, msgName);
        }
        String dbName = this.genDbName(mappedStatement);
        long duration = span.getTimeUsedMicros();
        String traceId = trace.getTraceId();
        span.changeAction("DB." + sqlId);
        if (duration >= EnvConfigProps.slowSqlMillis * 1000L) {
            log.warn("slow sql:" + AsyncAuditLog.escapeText(sql) + ", ts=" + duration + ", traceId=" + traceId);
        }
        if (msgName.isEmpty()) {
            msgName = "DB." + sqlId;
        }
        String extraInfo = "db:" + dbName;
        String timestamp = AsyncAuditLog.now(ctx, span);
        Map<String, Object> reqParams = new LinkedHashMap<>();
        reqParams.put("sql", sql);
        Log(
                AsyncAuditLog.dbAuditLog,
                generateLogMapWithExtraInfo(
                        timestamp,
                        traceId,
                        span.getSpanId(),
                        span.getRootSpan().getAction(),
                        genConnId(mappedStatement),
                        new Action("DB", msgName),
                        sequence,
                        duration / 1000 + "ms",
                        reqParams,
                        this.genResParams(result),
                        genException(throwable),
                        extraInfo,
                        EnvConfigProps.dbLogMaxChars));
    }

    Map<String, Object> genResParams(Object result) {
        int rc = 1;
        if (result instanceof List) {
            // 处理返回值为 List 的情况
            List<?> resultList = (List<?>) result;
            rc = resultList.size();
        }
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("list", result);
        res.put("rowcount", rc);
        return res;
    }

    private String genDbName(MappedStatement mappedStatement) {
        String jdbcUrl = "";
        DataSource dataSource =
                mappedStatement.getConfiguration().getEnvironment().getDataSource();
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource druidDataSource = (HikariDataSource) dataSource;
            jdbcUrl = druidDataSource.getJdbcUrl();
        }
        if (dataSource instanceof DruidDataSource) {
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
            jdbcUrl = druidDataSource.getRawJdbcUrl();
        }
        return JdbcUrlUtil.findDataBaseNameByUrl(jdbcUrl);
    }

    private String genConnId(MappedStatement mappedStatement) {
        DataSource dataSource =
                mappedStatement.getConfiguration().getEnvironment().getDataSource();
        String jdbcUrl = "";
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource druidDataSource = (HikariDataSource) dataSource;
            jdbcUrl = druidDataSource.getJdbcUrl();
        }
        if (dataSource instanceof DruidDataSource) {
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
            jdbcUrl = druidDataSource.getRawJdbcUrl();
        }
        return JdbcUrlUtil.findConnIdByUrl(jdbcUrl);
    }
}
