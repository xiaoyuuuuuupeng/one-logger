package fun.pxyc.onelogger.redis.proxy;

import fun.pxyc.onelogger.redis.RedisLogFormat;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;

public class BoundHashOperationsProxy<H, HK, HV> implements BoundHashOperations<H, HK, HV> {
    private final BoundHashOperations<H, HK, HV> boundHashOps;

    public BoundHashOperationsProxy(BoundHashOperations<H, HK, HV> boundHashOps) {
        this.boundHashOps = boundHashOps;
    }

    public Long delete(Object... keys) {
        Span span = Trace.startAsync("REDIS", "boundHash_delete");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundHashOps.delete(keys);
            span.stop(true);
            this.log(o, span, "boundHash_delete", ctx, null, keys);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundHash_delete", ctx, throwable, keys);
            throw throwable;
        }
    }

    public Boolean hasKey(Object key) {
        Span span = Trace.startAsync("REDIS", "boundHash_hasKey");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundHashOps.hasKey(key);
            span.stop(true);
            this.log(o, span, "boundHash_hasKey", ctx, null, key);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_hasKey", ctx, t, key);
            throw t;
        }
    }

    public HV get(Object member) {
        Span span = Trace.startAsync("REDIS", "boundHash_get");
        TraceContext ctx = Trace.currentContext();

        try {
            HV o = this.boundHashOps.get(member);
            span.stop(true);
            this.log(o, span, "boundHash_get", ctx, null, member);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_get", ctx, t, member);
            throw t;
        }
    }

    public List<HV> multiGet(Collection<HK> keys) {
        Span span = Trace.startAsync("REDIS", "boundHash_multiGet");
        TraceContext ctx = Trace.currentContext();

        try {
            List<HV> o = this.boundHashOps.multiGet(keys);
            span.stop(true);
            this.log(o, span, "boundHash_multiGet", ctx, null, keys);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_multiGet", ctx, t, keys);
            throw t;
        }
    }

    public Long increment(HK key, long delta) {
        Span span = Trace.startAsync("REDIS", "boundHash_increment");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundHashOps.increment(key, delta);
            span.stop(true);
            this.log(o, span, "boundHash_increment", ctx, null, key, delta);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_increment", ctx, t, key, delta);
            throw t;
        }
    }

    public Double increment(HK key, double delta) {
        Span span = Trace.startAsync("REDIS", "boundHash_increment");
        TraceContext ctx = Trace.currentContext();

        try {
            Double o = this.boundHashOps.increment(key, delta);
            span.stop(true);
            this.log(o, span, "boundHash_increment", ctx, null, key, delta);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_increment", ctx, t, key, delta);
            throw t;
        }
    }

    @Override
    public HK randomKey() {
        Span span = Trace.startAsync("REDIS", "boundHash_randomKey");
        TraceContext ctx = Trace.currentContext();

        try {
            HK o = this.boundHashOps.randomKey();
            span.stop(true);
            this.log(o, span, "boundHash_randomKey", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_randomKey", ctx, t);
            throw t;
        }
    }

    @Override
    public Map.Entry<HK, HV> randomEntry() {
        Span span = Trace.startAsync("REDIS", "boundHash_randomEntry");
        TraceContext ctx = Trace.currentContext();

        try {
            Map.Entry<HK, HV> o = this.boundHashOps.randomEntry();
            span.stop(true);
            this.log(o, span, "boundHash_randomEntry", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_randomEntry", ctx, t);
            throw t;
        }
    }

    @Override
    public List<HK> randomKeys(long count) {
        Span span = Trace.startAsync("REDIS", "boundHash_randomKeys");
        TraceContext ctx = Trace.currentContext();

        try {
            List<HK> o = this.boundHashOps.randomKeys(count);
            span.stop(true);
            this.log(o, span, "boundHash_randomKeys", ctx, null, count);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_randomKeys", ctx, t, count);
            throw t;
        }
    }

    @Override
    public Map<HK, HV> randomEntries(long count) {
        Span span = Trace.startAsync("REDIS", "boundHash_randomEntries");
        TraceContext ctx = Trace.currentContext();

        try {
            Map<HK, HV> o = this.boundHashOps.randomEntries(count);
            span.stop(true);
            this.log(o, span, "boundHash_randomEntries", ctx, null, count);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_randomEntries", ctx, t, count);
            throw t;
        }
    }

    public Set<HK> keys() {
        Span span = Trace.startAsync("REDIS", "boundHash_keys");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<HK> o = this.boundHashOps.keys();
            span.stop(true);
            this.log(o, span, "boundHash_keys", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_keys", ctx, t);
            throw t;
        }
    }

    public Long lengthOfValue(HK hk) {
        Span span = Trace.startAsync("REDIS", "boundHash_lengthOfValue");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundHashOps.lengthOfValue(hk);
            span.stop(true);
            this.log(o, span, "boundHash_lengthOfValue", ctx, null, hk);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_lengthOfValue", ctx, t, hk);

            throw t;
        }
    }

    public Long size() {
        Span span = Trace.startAsync("REDIS", "boundHash_size");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundHashOps.size();
            span.stop(true);
            this.log(o, span, "boundHash_size", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_size", ctx, t);

            throw t;
        }
    }

    public void putAll(Map<? extends HK, ? extends HV> m) {
        Span span = Trace.startAsync("REDIS", "boundHash_putAll");
        TraceContext ctx = Trace.currentContext();

        try {
            this.boundHashOps.putAll(m);
            span.stop(true);
            this.log(null, span, "boundHash_putAll", ctx, null, m);
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_putAll", ctx, t, m);

            throw t;
        }
    }

    public void put(HK key, HV value) {
        Span span = Trace.startAsync("REDIS", "boundHash_put");
        TraceContext ctx = Trace.currentContext();

        try {
            this.boundHashOps.put(key, value);
            span.stop(true);
            this.log(null, span, "boundHash_put", ctx, null, key, value);
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_put", ctx, t, key, value);

            throw t;
        }
    }

    public Boolean putIfAbsent(HK key, HV value) {
        Span span = Trace.startAsync("REDIS", "boundHash_putIfAbsent");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundHashOps.putIfAbsent(key, value);
            span.stop(true);
            this.log(o, span, "boundHash_putIfAbsent", ctx, null, key, value);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_putIfAbsent", ctx, t, key, value);

            throw t;
        }
    }

    public List<HV> values() {
        Span span = Trace.startAsync("REDIS", "boundHash_values");
        TraceContext ctx = Trace.currentContext();

        try {
            List<HV> o = this.boundHashOps.values();
            span.stop(true);
            this.log(o, span, "boundHash_values", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_values", ctx, t);

            throw t;
        }
    }

    public Map<HK, HV> entries() {
        Span span = Trace.startAsync("REDIS", "boundHash_entries");
        TraceContext ctx = Trace.currentContext();

        try {
            Map<HK, HV> o = this.boundHashOps.entries();
            span.stop(true);
            this.log(o, span, "boundHash_entries", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_entries", ctx, t);

            throw t;
        }
    }

    public Cursor<Map.Entry<HK, HV>> scan(ScanOptions options) {
        Span span = Trace.startAsync("REDIS", "boundHash_scan");
        TraceContext ctx = Trace.currentContext();

        try {
            Cursor<Map.Entry<HK, HV>> o = this.boundHashOps.scan(options);
            span.stop(true);
            this.log(o, span, "boundHash_scan", ctx, null, options);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_scan", ctx, t, options);

            throw t;
        }
    }

    public RedisOperations<H, ?> getOperations() {
        return this.boundHashOps.getOperations();
    }

    public H getKey() {
        return this.boundHashOps.getKey();
    }

    public DataType getType() {
        return this.boundHashOps.getType();
    }

    public Long getExpire() {
        Span span = Trace.startAsync("REDIS", "boundHash_getExpire");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundHashOps.getExpire();
            span.stop(true);
            this.log(o, span, "boundHash_getExpire", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_getExpire", ctx, t);

            throw t;
        }
    }

    public Boolean expire(long timeout, TimeUnit unit) {
        Span span = Trace.startAsync("REDIS", "boundHash_expire");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundHashOps.expire(timeout, unit);
            span.stop(true);
            this.log(o, span, "boundHash_expire", ctx, null, timeout, unit);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_expire", ctx, t, timeout, unit);

            throw t;
        }
    }

    public Boolean expireAt(Date date) {
        Span span = Trace.startAsync("REDIS", "boundHash_expireAt");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundHashOps.expireAt(date);
            span.stop(true);
            this.log(o, span, "boundHash_expireAt", ctx, null, date);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_expireAt", ctx, t, date);

            throw t;
        }
    }

    public Boolean persist() {
        Span span = Trace.startAsync("REDIS", "boundHash_persist");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundHashOps.persist();
            span.stop(true);
            this.log(o, span, "boundHash_persist", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_persist", ctx, t);

            throw t;
        }
    }

    public void rename(H newKey) {

        Span span = Trace.startAsync("REDIS", "boundHash_rename");
        TraceContext ctx = Trace.currentContext();

        try {
            this.boundHashOps.rename(newKey);
            span.stop(true);
            this.log(null, span, "boundHash_rename", ctx, null, newKey);
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundHash_rename", ctx, t, newKey);

            throw t;
        }
    }

    void log(Object res, Span span, String serviceName, TraceContext ctx, Throwable throwable, Object... param) {
        RedisLogFormat.log(res, span, serviceName, ctx, throwable, this.toArray(this.boundHashOps.getKey(), param));
    }

    Object[] toArray(Object key, Object... param) {
        if (param != null && param.length != 0) {
            Object[] newarray = new Object[param.length + 1];
            newarray[0] = key;

            System.arraycopy(param, 0, newarray, 1, param.length);

            return newarray;
        } else {
            return new Object[] {key};
        }
    }
}
