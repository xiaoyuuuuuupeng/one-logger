package fun.pxyc.onelogger.utils;

import java.lang.reflect.Constructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionUtils {
    private static final Logger log = LoggerFactory.getLogger(ReflectionUtils.class);

    public static Class<?> getClass(String s) {
        try {
            return Class.forName(s);
        } catch (Throwable e) {
            return null;
        }
    }

    public static Object newObject(String clsName) {
        try {
            Class<?> cls = Class.forName(clsName);
            Constructor<?> cons = cls.getDeclaredConstructor();
            return cons.newInstance();
        } catch (Throwable e) {
            throw new RuntimeException("newObject exception", e);
        }
    }
}
