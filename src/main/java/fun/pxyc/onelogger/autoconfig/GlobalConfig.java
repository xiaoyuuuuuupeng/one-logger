package fun.pxyc.onelogger.autoconfig;

import fun.pxyc.onelogger.utils.NetUtils;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalConfig {
    public static final String sourceIp = NetUtils.getHostAddress();
    public static String globalProfile = "";
    public static String globalGitName = "";
    public static int globalServiceId = 999;
    public static String globalServiceName = "";
    public static Integer dubboPort = 0;

    public GlobalConfig() {}
}
