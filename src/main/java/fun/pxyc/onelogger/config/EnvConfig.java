package fun.pxyc.onelogger.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import fun.pxyc.onelogger.autoconfig.GlobalConfig;
import fun.pxyc.onelogger.utils.YamlParser;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootVersion;

public class EnvConfig {

    public EnvConfig() {}

    public static void initEnv() {
        try {
            initEnvInternal();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeAllStdoutAppenders() {
        if (!GlobalConfig.globalProfile.equals("local") && !GlobalConfig.globalProfile.equals("dev")) {
            Logger rootLog = (Logger) LoggerFactory.getLogger("console");
            if (GlobalConfig.globalProfile.equals("prd")) {
                if (rootLog != null
                        && rootLog.getLevel() != null
                        && rootLog.getLevel().equals(Level.DEBUG)) {
                    rootLog.setLevel(Level.INFO);
                }

                Logger log3 = (Logger) LoggerFactory.getLogger("com.sqq");
                if (log3 != null && log3.getLevel() != null && log3.getLevel().equals(Level.DEBUG)) {
                    log3.setLevel(Level.INFO);
                }
            }

            if (rootLog != null) {
                List<Logger> list = rootLog.getLoggerContext().getLoggerList();
                if (list != null) {
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Logger l = (Logger) iterator.next();
                        l.detachAppender("STDOUT");
                    }
                }
            }
        }
    }

    private static void initEnvInternal() {
        org.slf4j.Logger log = LoggerFactory.getLogger(EnvConfig.class);
        log.info("spring boot version: " + SpringBootVersion.getVersion());
        log.info("envconfig version 0.1.1 build @ 20240424");
    }

    private static String convertEnv(String profile) {
        return profile.toLowerCase();
    }

    private static Properties loadProfileProperties(String profile) throws Exception {
        String file = "application-" + profile + ".properties";
        if (profile == null || profile.isEmpty()) {
            file = "application.properties";
        }
        Properties p = new Properties();
        InputStream in = null;
        try {
            in = EnvConfig.class.getClassLoader().getResourceAsStream(file);
        } catch (Exception e) {
        }
        if (in != null) {
            p.load(in);
            in.close();
        }
        Properties p1 = loadYamlFile(profile);
        p.putAll(p1);
        return p;
    }

    private static Properties loadYamlFile(String profile) throws Exception {
        String file = "application-" + profile + ".yaml";
        if (profile == null || profile.isEmpty()) {
            file = "application.yaml";
        }
        InputStream in = null;
        try {
            in = EnvConfig.class.getClassLoader().getResourceAsStream(file);
        } catch (Exception exception) {

        }
        Properties p = new Properties();
        if (in != null) {
            p.putAll(YamlParser.yamlToFlattenedMap(in));
            in.close();
        }
        file = file.replaceAll("yaml", "yml");
        try {
            InputStream in1 = EnvConfig.class.getClassLoader().getResourceAsStream(file);
            if (in1 != null) {
                p.putAll(YamlParser.yamlToFlattenedMap(in1));
            }
        } catch (Exception exception) {

        }
        return p;
    }
}
