package fun.pxyc.onelogger.log;

import fun.pxyc.onelogger.utils.ReflectionUtils;
import java.util.*;

public class Spis {

    public static HashMap<String, List<PluginInfo>> plugins = new HashMap<>();

    public static <T> List<T> getPlugins(Class<T> cls) {
        List<PluginInfo> pluginInfos = plugins.get(cls.getName());
        List<T> ps = new ArrayList<>();
        for (PluginInfo pi : pluginInfos) {
            if (pi.bean != null) ps.add((T) pi.bean);
            pi.bean = ReflectionUtils.newObject(pi.impCls.getName());
            ps.add((T) pi.bean);
        }
        return ps;
    }

    public static <T> T getPlugin(Class<T> cls, String params) {

        List<PluginInfo> list = plugins.get(cls.getName());
        if (list == null) return null;
        String type = parseType(params);
        for (PluginInfo pi : list) {
            if (pi.matchNames.contains(type)) {
                if (pi.bean != null) return (T) pi.bean;
                pi.bean = ReflectionUtils.newObject(pi.impCls.getName());
                String t = parseParams(params);
                return (T) pi.bean;
            }
        }
        return null;
    }

    public static String parseParams(String s) {
        int p = s.indexOf(":");
        if (p >= 0) return s.substring(p + 1);
        return "";
    }

    public static String parseType(String s) {
        s = s.toLowerCase();
        int p = s.indexOf(":");
        if (p >= 0) return s.substring(0, p);
        return s;
    }

    public static class PluginInfo {
        Class cls;
        Class impCls;
        Object bean;
        Set<String> matchNames = new HashSet<>();

        public PluginInfo(Class cls, Class impCls) {
            this(cls, impCls, null, null);
        }

        public PluginInfo(Class cls, Class impCls, Object bean, String beanName) {
            this.cls = cls;
            this.impCls = impCls;
            this.bean = bean;

            String suffix = cls.getSimpleName().toLowerCase();

            if (beanName != null) matchNames.add(beanName);
            matchNames.add(impCls.getName().toLowerCase());
            matchNames.add(impCls.getSimpleName().toLowerCase());
            matchNames.add(removeSuffix(impCls.getSimpleName().toLowerCase(), suffix));
        }

        String removeSuffix(String s, String suffix) {
            if (s.endsWith(suffix)) {
                return s.substring(0, s.length() - suffix.length());
            }
            return s;
        }
    }
}
