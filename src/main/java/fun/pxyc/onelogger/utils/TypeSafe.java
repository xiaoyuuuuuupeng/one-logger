package fun.pxyc.onelogger.utils;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TypeSafe {
    public TypeSafe() {}

    public static String anyToString(Object v) {
        if (v == null) {
            return null;
        } else {
            return v instanceof String ? (String) v : v.toString();
        }
    }

    public static String anyToString(Object v, String defaultValue) {
        if (v == null) {
            return defaultValue;
        } else {
            return v instanceof String ? (String) v : v.toString();
        }
    }

    public static int anyToInt(Object v) {
        return anyToInt(v, 0);
    }

    public static int anyToInt(Object v, int defaultValue) {
        if (v == null) {
            return defaultValue;
        } else if (v instanceof Integer) {
            return (Integer) v;
        } else if (v instanceof Number) {
            return ((Number) v).intValue();
        } else {
            try {
                return Integer.parseInt(v.toString());
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }

    public static long anyToLong(Object v) {
        return anyToLong(v, 0L);
    }

    public static long anyToLong(Object v, long defaultValue) {
        if (v == null) {
            return defaultValue;
        } else if (v instanceof Integer) {
            return (long) (Integer) v;
        } else if (v instanceof Long) {
            return (Long) v;
        } else if (v instanceof Number) {
            return ((Number) v).longValue();
        } else {
            try {
                return Long.parseLong(v.toString());
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }

    public static float anyToFloat(Object v) {
        return anyToFloat(v, 0.0F);
    }

    public static float anyToFloat(Object v, float defaultValue) {
        if (v == null) {
            return defaultValue;
        } else if (v instanceof Float) {
            return (Float) v;
        } else if (v instanceof Number) {
            return ((Number) v).floatValue();
        } else {
            try {
                return Float.parseFloat(v.toString());
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }

    public static boolean anyToBool(Object v) {
        return anyToBool(v, false);
    }

    public static boolean anyToBool(Object v, boolean defaultValue) {
        if (v != null && !v.equals("")) {
            if (v instanceof Boolean) {
                return (Boolean) v;
            } else if (v instanceof Number) {
                return ((Number) v).intValue() == 1;
            } else {
                try {
                    String s = v.toString().toLowerCase();
                    if (!s.equals("true") && !s.equals("yes") && !s.equals("t") && !s.equals("y") && !s.equals("1")) {
                        return !s.equals("false")
                                && !s.equals("no")
                                && !s.equals("f")
                                && !s.equals("n")
                                && !s.equals("0")
                                && defaultValue;
                    } else {
                        return true;
                    }
                } catch (Exception e) {
                    return defaultValue;
                }
            }
        } else {
            return defaultValue;
        }
    }

    public static double anyToDouble(Object v) {
        return anyToDouble(v, 0.0);
    }

    public static double anyToDouble(Object v, double defaultValue) {
        if (v == null) {
            return defaultValue;
        } else if (v instanceof Double) {
            return (Double) v;
        } else if (v instanceof Number) {
            return ((Number) v).doubleValue();
        } else {
            try {
                return Double.parseDouble(v.toString());
            } catch (Exception e) {
                return 0.0;
            }
        }
    }

    public static BigInteger anyToBigInteger(Object v) {
        if (v == null) {
            return BigInteger.ZERO;
        } else {
            try {
                return new BigInteger(v.toString());
            } catch (Exception e) {
                return BigInteger.ZERO;
            }
        }
    }

    public static Map<String, Object> anyToMap(Object v) {
        if (v == null) {
            return null;
        } else {
            return v instanceof Map ? (Map) v : null;
        }
    }

    public static List<Object> anyToList(Object v) {
        if (v == null) {
            return null;
        } else {
            return v instanceof List ? (List) v : null;
        }
    }

    public static Date anyToDate(Object v) {
        if (v == null) {
            return null;
        } else if (v instanceof Date) {
            return (Date) v;
        } else if (v instanceof Long) {
            return new Date((Long) v);
        } else if (v instanceof String) {
            String s = (String) v;
            if (s.isEmpty()) {
                return null;
            } else if (s.contains("-")) {
                return parseDate(s);
            } else {
                long l = anyToLong(s);
                return l == 0L ? null : new Date(l);
            }
        } else {
            return null;
        }
    }

    static Date parseDate(String s) {
        try {
            SimpleDateFormat f;
            if (s.length() == 19) {
                f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            } else {
                if (s.length() != 10) {
                    return null;
                }

                f = new SimpleDateFormat("yyyy-MM-dd");
            }

            return f.parse(s);
        } catch (Exception e) {
            return null;
        }
    }
}
