package fun.pxyc.onelogger.autoconfig;

import fun.pxyc.onelogger.auditlog.AsyncAuditLogPool;
import fun.pxyc.onelogger.config.EnvConfig;
import fun.pxyc.onelogger.log.*;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.adpater.DefaultTraceAdapter;
import fun.pxyc.onelogger.trace.adpater.TraceAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
@EnableConfigurationProperties(EnvConfigProps.class)
public class EnvAutoConfiguration implements ApplicationListener<ApplicationEvent> {
    private static final Logger log = LoggerFactory.getLogger(EnvAutoConfiguration.class);

    @Value("${spring.profiles.active:dev}")
    String globalProfile = "";

    @Value("${server.port:8080}")
    int serverPort;

    @Value("${spring.application.name:unknown}")
    String springApplicationName;

    public EnvAutoConfiguration() {}

    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            GlobalConfig.globalServiceName = springApplicationName;
            // TODO serviceId:未配 采用端口号
            GlobalConfig.globalServiceId = serverPort;
            GlobalConfig.globalProfile = globalProfile;
            EnvConfig.initEnv();
            if (EnvConfigProps.disableStdout) {
                EnvConfig.removeAllStdoutAppenders();
            }
            // 拉取spi
            loadSpi();
            TraceAdapter traceAdapter = Spis.getPlugin(TraceAdapter.class, EnvConfigProps.traceAdapterParam);
            if (traceAdapter == null) {
                Trace.setAdapter(newTraceAdapter());
            } else {
                Trace.setAdapter(traceAdapter);
            }
            LoggerFormatterHandler loggerFormatterHandler =
                    Spis.getPlugin(LoggerFormatterHandler.class, EnvConfigProps.logFormatter);
            if (loggerFormatterHandler == null) {
                loggerFormatterHandler = new JsonLoggerFormatterHandler();
            }
            LoggerFormatter.setHandler(loggerFormatterHandler);
            // 获取filter spi
            List<LoggerFilter> filters = Spis.getPlugins(LoggerFilter.class);
            IgnoreLoggerFilter ignoreLoggerFilter = new IgnoreLoggerFilter();
            ignoreLoggerFilter.setIgnoreParams(convertToList(EnvConfigProps.ignoreParams));
            filters.add(ignoreLoggerFilter);
            IndexLoggerFilter indexLoggerFilter = new IndexLoggerFilter();
            indexLoggerFilter.setLogIndex(convertToList(EnvConfigProps.logIndex));
            filters.add(indexLoggerFilter);
            LoggerFormatter.setFilters(filters);
        }
    }

    private List<String> convertToList(String params) {
        String[] indexs = new String[] {params};
        if (params.contains(";")) {
            indexs = params.split(";");
        }
        return Arrays.asList(indexs);
    }

    private void loadSpi() {
        try {
            loadSpi(LoggerFormatterHandler.class);
            loadSpi(TraceAdapter.class);
            loadSpi(LoggerFilter.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSpi(Class cls) throws Exception {
        Enumeration<URL> urls = getClass().getClassLoader().getResources("META-INF/services/" + cls.getName());
        List<Spis.PluginInfo> list = new ArrayList<>();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            List<String> lines = readSpiLines(url);
            for (String s : lines) {
                if (s.trim().isEmpty()) continue;
                try {
                    Class implCls = Class.forName(s);
                    Spis.PluginInfo pi = new Spis.PluginInfo(cls, implCls);
                    list.add(pi);
                } catch (Throwable e) {
                }
            }
        }
        Spis.plugins.put(cls.getName(), list);
    }

    List<String> readSpiLines(URL url) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String s = in.readLine();
            while (s != null) {
                if (!s.isEmpty()) {
                    lines.add(s);
                }
                s = in.readLine();
            }
            return lines;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TraceAdapter newTraceAdapter() {
        Trace.setAppName(GlobalConfig.globalServiceName);
        return new DefaultTraceAdapter();
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    AsyncAuditLogPool asyncLogPool() {
        return new AsyncAuditLogPool();
    }
}
