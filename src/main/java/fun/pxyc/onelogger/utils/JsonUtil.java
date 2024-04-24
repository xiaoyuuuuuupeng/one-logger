package fun.pxyc.onelogger.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Gson gson = new Gson();

    static {
        mapper.setSerializationInclusion(Include.NON_NULL);
    }

    public JsonUtil() {}

    public static String toJson(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            try {
                return gsonParser(o);
            } catch (Exception e1) {
                log.error("json convert exception", e1);
            }
        }
        return null;
    }

    public static String gsonParser(Object o) {
        try {
            return gson.toJson(o);
        } catch (Exception e) {
            log.error("json convert exception", e);
            return null;
        }
    }

    public static Object toJsonObject(Object o) {
        if (o == null) {
            return "";
        } else if (o instanceof String) {
            return o;
        } else if (o instanceof Number) {
            return o.toString();
        } else if (o instanceof TimeUnit) {
            return o.toString();
        } else if (o instanceof Map) {
            return toMap(toJson(o));
        } else if (o instanceof List) {
            return toList(toJson(o));
        } else {
            if (o instanceof Date) {
                String s = o.toString();
                int p = s.lastIndexOf(".");
                return p >= 0 ? s.substring(0, p) : s;
            } else {
                String s = JsonUtil.toJson(o);
                return s == null ? o.toString() : s;
            }
        }
    }

    public static Map<String, Object> toJsonMap(Object o) {
        HashMap<String, Object> map = new HashMap<>();
        if (o == null) {
            return map;
        } else if (o instanceof String) {
            map.put("data", o);
            return map;
        } else if (o instanceof Number) {
            map.put("number", o);
            return map;
        } else if (o instanceof TimeUnit) {
            map.put("timeunit", o);
            return map;
        } else if (o instanceof Map) {
            return toMap(toJson(o));
        } else if (o instanceof List) {
            List<Object> objects = toList(toJson(o));
            map.put("list", objects);
            return map;
        } else {
            if (o instanceof Date) {
                String s = o.toString();
                int p = s.lastIndexOf(".");
                map.put("date", p >= 0 ? s.substring(0, p) : s);
                return map;
            } else {
                return toMap(toJson(o));
            }
        }
    }

    public static Map<String, Object> toMap(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        try {
            Map<String, Object> results = mapper.readValue(s, new TypeReference<Map<String, Object>>() {});
            return results;
        } catch (Exception e) {
            log.error("json convert exception", e);
            return null;
        }
    }

    public static Map<String, Object> findJsonFieldValue(String json, List<String> jsonField) {
        Map<String, Object> indexValue = new LinkedHashMap<>();
        for (String index : jsonField) {
            if (StringUtils.isEmpty(index)) {
                continue;
            }
            try {
                Object read = JsonPath.parse(json).read(index, String.class);
                if (read != null) {
                    indexValue.put(index, read);
                }
            } catch (Exception e) {
            }
        }
        return indexValue;
    }

    public static HashMap<String, Object> toHashMap(String s) {
        try {
            HashMap<String, Object> results = mapper.readValue(s, new TypeReference<HashMap<String, Object>>() {});
            return results;
        } catch (Exception e) {
            log.error("json convert exception", e);
            return null;
        }
    }

    public static LinkedHashMap<String, Object> toLinkedHashMap(String s) {
        try {
            return mapper.readValue(s, new TypeReference<LinkedHashMap<String, Object>>() {});
        } catch (Exception e) {
            log.error("json convert exception", e);
            return null;
        }
    }

    public static TreeMap<String, Object> toTreeMap(String s) {
        try {
            return mapper.readValue(s, new TypeReference<TreeMap<String, Object>>() {});
        } catch (Exception e) {
            log.error("json convert exception", e);
            return null;
        }
    }

    public static List<Object> toList(String s) {
        try {
            return mapper.<List>readValue(s, new TypeReference<List<Object>>() {});
        } catch (Exception e) {
            log.error("json convert exception", e);
            return null;
        }
    }

    public static ArrayList<Object> toArrayList(String s) {
        try {
            return mapper.readValue(s, new TypeReference<ArrayList<Object>>() {});
        } catch (Exception e) {
            log.error("json convert exception", e);
            return null;
        }
    }

    public static LinkedList<Object> toLinkedList(String s) {
        try {
            return mapper.readValue(s, new TypeReference<LinkedList<Object>>() {});
        } catch (Exception e) {
            log.error("json convert exception", e);
            return null;
        }
    }

    public static Object toRawObject(String s, Class cls) {
        try {
            Object results = mapper.readValue(s, cls);
            return results;
        } catch (Exception e) {
            log.error("json convert exception", e);
            return null;
        }
    }

    public static <T> T toObject(String s, Class<T> cls) {
        try {
            T results = mapper.readValue(s, cls);
            return results;
        } catch (Exception e) {
            log.error("json convert exception", e);
            return null;
        }
    }

    public static <T> T toObject(String s, TypeReference<T> tr) {
        try {
            T results = mapper.readValue(s, tr);
            return results;
        } catch (Exception e) {
            log.error("json convert exception", e);
            return null;
        }
    }
}
