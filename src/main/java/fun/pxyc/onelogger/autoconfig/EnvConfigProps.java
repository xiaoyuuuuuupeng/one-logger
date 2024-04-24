package fun.pxyc.onelogger.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "logger.config")
public class EnvConfigProps {

    public static int slowSqlMillis = 500;
    public static int slowRedisMillis = 50;
    public static int slowHttpMillis = 1000;
    public static int slowDubboMillis = 1000;
    public static int slowKafkaMillis = 50;
    public static int slowMqMillis = 50;
    public static int slowHbaseMillis = 5000;
    public static int slowMongoMillis = 200;
    public static int httpLogMaxChars = -1;
    public static int dubboLogMaxChars = -1;
    public static int redisLogMaxChars = -1;
    public static int bizLogMaxChars = -1;
    public static int esLogMaxChars = -1;
    public static int dbLogMaxChars = -1;
    public static int hbaseLogMaxChars = -1;
    public static int mqLogMaxChars = -1;
    public static int mongoLogMaxChars = -1;
    public static int webLogMaxChars = -1;
    public static int restTemplateLogMaxChars = -1;
    public static int konisGraphLogMaxChars = -1;
    public static int kafkaLogMaxChars = -1;
    public static boolean disableStdout = false;
    public static String traceAdapterParam = "default";
    public static String logFormatter = "json";
    public static String logIndex = "";
    public static String ignoreParams = "";
    public static String bizCutPoint =
            "execution(* com.demo.service.impl..*.*(..)) ||execution(* com.demo.bizService.remoteImpl..*.*(..)) || execution(* com.demo.bizService.impl..*(..)) || execution(* com.sqg.dubboService..*(..)) || execution(* com.demo.handler..*(..)) || execution(* com.demo.cacheService..*(..))";
    public static String webCutPoint = "execution(* com.demo.controller..*(..))";
    public static int sampleRate = 100;
    public static boolean reqHeaders = true;

    public EnvConfigProps() {}

    public int getSlowSqlMillis() {
        return slowSqlMillis;
    }

    public void setSlowSqlMillis(int slowSqlMillis) {
        EnvConfigProps.slowSqlMillis = slowSqlMillis;
    }

    public int getSlowRedisMillis() {
        return slowRedisMillis;
    }

    public void setSlowRedisMillis(int slowRedisMillis) {
        EnvConfigProps.slowRedisMillis = slowRedisMillis;
    }

    public int getSlowHttpMillis() {
        return slowHttpMillis;
    }

    public void setSlowHttpMillis(int slowHttpMillis) {
        EnvConfigProps.slowHttpMillis = slowHttpMillis;
    }

    public int getSlowDubboMillis() {
        return slowDubboMillis;
    }

    public void setSlowDubboMillis(int slowDubboMillis) {
        EnvConfigProps.slowDubboMillis = slowDubboMillis;
    }

    public int getBizLogMaxChars() {
        return bizLogMaxChars;
    }

    public void setBizLogMaxChars(int bizLogMaxChars) {
        EnvConfigProps.bizLogMaxChars = bizLogMaxChars;
    }

    public int getSlowKafkaMillis() {
        return slowKafkaMillis;
    }

    public void setSlowKafkaMillis(int slowKafkaMillis) {
        EnvConfigProps.slowKafkaMillis = slowKafkaMillis;
    }

    public int getSlowMqMillis() {
        return slowMqMillis;
    }

    public void setSlowMqMillis(int slowMqMillis) {
        EnvConfigProps.slowMqMillis = slowMqMillis;
    }

    public int getSlowHbaseMillis() {
        return slowHbaseMillis;
    }

    public void setSlowHbaseMillis(int slowHbaseMillis) {
        EnvConfigProps.slowHbaseMillis = slowHbaseMillis;
    }

    public int getSlowMongoMillis() {
        return slowMongoMillis;
    }

    public void setSlowMongoMillis(int slowMongoMillis) {
        EnvConfigProps.slowMongoMillis = slowMongoMillis;
    }

    public int getHttpLogMaxChars() {
        return httpLogMaxChars;
    }

    public void setHttpLogMaxChars(int httpLogMaxChars) {
        EnvConfigProps.httpLogMaxChars = httpLogMaxChars;
    }

    public int getDubboLogMaxChars() {
        return dubboLogMaxChars;
    }

    public void setDubboLogMaxChars(int dubboLogMaxChars) {
        EnvConfigProps.dubboLogMaxChars = dubboLogMaxChars;
    }

    public int getRedisLogMaxChars() {
        return redisLogMaxChars;
    }

    public void setRedisLogMaxChars(int redisLogMaxChars) {
        EnvConfigProps.redisLogMaxChars = redisLogMaxChars;
    }

    public int getDbLogMaxChars() {
        return dbLogMaxChars;
    }

    public void setDbLogMaxChars(int dbLogMaxChars) {
        EnvConfigProps.dbLogMaxChars = dbLogMaxChars;
    }

    public int getHbaseLogMaxChars() {
        return hbaseLogMaxChars;
    }

    public void setHbaseLogMaxChars(int hbaseLogMaxChars) {
        EnvConfigProps.hbaseLogMaxChars = hbaseLogMaxChars;
    }

    public int getMqLogMaxChars() {
        return mqLogMaxChars;
    }

    public void setMqLogMaxChars(int mqLogMaxChars) {
        EnvConfigProps.mqLogMaxChars = mqLogMaxChars;
    }

    public int getMongoLogMaxChars() {
        return mongoLogMaxChars;
    }

    public void setMongoLogMaxChars(int mongoLogMaxChars) {
        EnvConfigProps.mongoLogMaxChars = mongoLogMaxChars;
    }

    public int getWebLogMaxChars() {
        return webLogMaxChars;
    }

    public void setWebLogMaxChars(int webLogMaxChars) {
        EnvConfigProps.webLogMaxChars = webLogMaxChars;
    }

    public int getEsLogMaxChars() {
        return esLogMaxChars;
    }

    public void setEsLogMaxChars(int esLogMaxChars) {
        EnvConfigProps.esLogMaxChars = esLogMaxChars;
    }

    public int getRestTemplateLogMaxChars() {
        return restTemplateLogMaxChars;
    }

    public void setRestTemplateLogMaxChars(int restTemplateLogMaxChars) {
        EnvConfigProps.restTemplateLogMaxChars = restTemplateLogMaxChars;
    }

    public int getKonisGraphLogMaxChars() {
        return konisGraphLogMaxChars;
    }

    public void setKonisGraphLogMaxChars(int konisGraphLogMaxChars) {
        EnvConfigProps.konisGraphLogMaxChars = konisGraphLogMaxChars;
    }

    public int getKafkaLogMaxChars() {
        return kafkaLogMaxChars;
    }

    public void setKafkaLogMaxChars(int kafkaLogMaxChars) {
        EnvConfigProps.kafkaLogMaxChars = kafkaLogMaxChars;
    }

    public boolean isDisableStdout() {
        return disableStdout;
    }

    public void setDisableStdout(boolean disableStdout) {
        EnvConfigProps.disableStdout = disableStdout;
    }

    public String getTraceAdapterParam() {
        return traceAdapterParam;
    }

    public void setTraceAdapterParam(String traceAdapterParam) {
        EnvConfigProps.traceAdapterParam = traceAdapterParam;
    }

    public String getLogFormatter() {
        return logFormatter;
    }

    public void setLogFormatter(String logFormatter) {
        EnvConfigProps.logFormatter = logFormatter;
    }

    public String getLogIndex() {
        return logIndex;
    }

    public void setLogIndex(String logIndex) {
        EnvConfigProps.logIndex = logIndex;
    }

    public String getIgnoreParams() {
        return ignoreParams;
    }

    public void setIgnoreParams(String ignoreParams) {
        EnvConfigProps.ignoreParams = ignoreParams;
    }

    public String getBizCutPoint() {
        return bizCutPoint;
    }

    public void setBizCutPoint(String bizCutPoint) {
        EnvConfigProps.bizCutPoint = bizCutPoint;
    }

    public String getWebCutPoint() {
        return webCutPoint;
    }

    public void setWebCutPoint(String webCutPoint) {
        EnvConfigProps.webCutPoint = webCutPoint;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        EnvConfigProps.sampleRate = sampleRate;
    }

    public boolean isReqHeaders() {
        return reqHeaders;
    }

    public void setReqHeaders(boolean reqHeaders) {
        EnvConfigProps.reqHeaders = reqHeaders;
    }
}
