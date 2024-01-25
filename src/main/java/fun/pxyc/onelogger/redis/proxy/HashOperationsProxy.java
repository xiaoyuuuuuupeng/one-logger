package fun.pxyc.onelogger.redis.proxy;

import fun.pxyc.onelogger.redis.RedisLogFormat;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;

public class HashOperationsProxy<K, HK, HV> implements HashOperations<K, HK, HV> {
    private final HashOperations<K, HK, HV> hashOps;

    public HashOperationsProxy(HashOperations<K, HK, HV> hashOps) {
        this.hashOps = hashOps;
    }

    public Long delete(K key, Object... hashKeys) {

        Span span = Trace.startAsync("REDIS", "hash_delete");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.hashOps.delete(key, hashKeys);
            span.stop(true);
            RedisLogFormat.log(o, span, "hash_delete", ctx, null, key, hashKeys);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_delete", ctx, t, key, hashKeys);

            throw t;
        }
    }

    public Boolean hasKey(K key, Object hashKey) {

        Span span = Trace.startAsync("REDIS", "hash_hasKey");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.hashOps.hasKey(key, hashKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "hash_hasKey", ctx, null, key, hashKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_hasKey", ctx, t, key, hashKey);

            throw t;
        }
    }

    public HV get(K key, Object hashKey) {

        Span span = Trace.startAsync("REDIS", "hash_get");
        TraceContext ctx = Trace.currentContext();

        try {
            HV o = this.hashOps.get(key, hashKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "hash_get", ctx, null, key, hashKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_get", ctx, t, key, hashKey);

            throw t;
        }
    }

    public List<HV> multiGet(K key, Collection<HK> hashKeys) {

        Span span = Trace.startAsync("REDIS", "hash_multiGet");
        TraceContext ctx = Trace.currentContext();

        try {
            List<HV> o = this.hashOps.multiGet(key, hashKeys);
            span.stop(true);
            RedisLogFormat.log(o, span, "hash_multiGet", ctx, null, key, hashKeys);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_multiGet", ctx, t, key, hashKeys);

            throw t;
        }
    }

    public Long increment(K key, HK hashKey, long delta) {

        Span span = Trace.startAsync("REDIS", "hash_increment");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.hashOps.increment(key, hashKey, delta);
            span.stop(true);
            RedisLogFormat.log(o, span, "hash_increment", ctx, null, key, hashKey, delta);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_increment", ctx, t, key, hashKey, delta);

            throw t;
        }
    }

    public Double increment(K key, HK hashKey, double delta) {

        Span span = Trace.startAsync("REDIS", "hash_increment");
        TraceContext ctx = Trace.currentContext();

        try {
            Double o = this.hashOps.increment(key, hashKey, delta);
            span.stop(true);
            RedisLogFormat.log(o, span, "hash_increment", ctx, null, key, hashKey, delta);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_increment", ctx, t, key, hashKey, delta);

            throw t;
        }
    }

    @Override
    public HK randomKey(K key) {
        Span span = Trace.startAsync("REDIS", "hash_randomKey");
        TraceContext ctx = Trace.currentContext();

        try {
            HK o = this.hashOps.randomKey(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "boundHash_randomKey", ctx, null, key);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_randomKey", ctx, t, key);
            throw t;
        }
    }

    @Override
    public Map.Entry<HK, HV> randomEntry(K key) {
        Span span = Trace.startAsync("REDIS", "hash_randomEntry");
        TraceContext ctx = Trace.currentContext();

        try {
            Map.Entry<HK, HV> o = this.hashOps.randomEntry(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "hash_randomEntry", ctx, null, key);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_randomEntry", ctx, t, key);
            throw t;
        }
    }

    @Override
    public List<HK> randomKeys(K key, long count) {
        Span span = Trace.startAsync("REDIS", "hash_randomKeys");
        TraceContext ctx = Trace.currentContext();

        try {
            List<HK> o = this.hashOps.randomKeys(key, count);
            span.stop(true);
            RedisLogFormat.log(o, span, "hash_randomKeys", ctx, null, key, count);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_randomKeys", ctx, t, key, count);
            throw t;
        }
    }

    @Override
    public Map<HK, HV> randomEntries(K key, long count) {
        Span span = Trace.startAsync("REDIS", "hash_randomEntries");
        TraceContext ctx = Trace.currentContext();

        try {
            Map<HK, HV> o = this.hashOps.randomEntries(key, count);
            span.stop(true);
            RedisLogFormat.log(o, span, "hash_randomEntries", ctx, null, key, count);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_randomEntries", ctx, t, key, count);
            throw t;
        }
    }

    public Set<HK> keys(K key) {

        Span span = Trace.startAsync("REDIS", "hash_keys");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<HK> o = this.hashOps.keys(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "hash_keys", ctx, null, key);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_keys", ctx, t, key);

            throw t;
        }
    }

    public Long lengthOfValue(K key, HK hk) {

        Span span = Trace.startAsync("REDIS", "hash_lengthOfValue");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.hashOps.lengthOfValue(key, hk);
            span.stop(true);
            RedisLogFormat.log(o, span, "hash_lengthOfValue", ctx, null, key, hk);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_lengthOfValue", ctx, t, key, hk);

            throw t;
        }
    }

    public Long size(K key) {

        Span span = Trace.startAsync("REDIS", "hash_size");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.hashOps.size(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "hash_size", ctx, null, key);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_size", ctx, t, key);

            throw t;
        }
    }

    public void putAll(K key, Map<? extends HK, ? extends HV> m) {

        Span span = Trace.startAsync("REDIS", "hash_putAll");
        TraceContext ctx = Trace.currentContext();

        try {
            this.hashOps.putAll(key, m);
            span.stop(true);
            RedisLogFormat.log(null, span, "hash_putAll", ctx, null, key, m);
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_putAll", ctx, t, key, m);

            throw t;
        }
    }

    public void put(K key, HK hashKey, HV value) {

        Span span = Trace.startAsync("REDIS", "hash_put");
        TraceContext ctx = Trace.currentContext();

        try {
            this.hashOps.put(key, hashKey, value);
            span.stop(true);
            RedisLogFormat.log(null, span, "hash_put", ctx, null, key, hashKey, value);
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_put", ctx, t, key, hashKey, value);

            throw t;
        }
    }

    public Boolean putIfAbsent(K key, HK hashKey, HV value) {

        Span span = Trace.startAsync("REDIS", "hash_putIfAbsent");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.hashOps.putIfAbsent(key, hashKey, value);
            span.stop(true);
            RedisLogFormat.log(o, span, "hash_putIfAbsent", ctx, null, key, hashKey, value);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_putIfAbsent", ctx, t, key, hashKey, value);

            throw t;
        }
    }

    public List<HV> values(K key) {

        Span span = Trace.startAsync("REDIS", "hash_values");
        TraceContext ctx = Trace.currentContext();

        try {
            List<HV> o = this.hashOps.values(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "hash_values", ctx, null, key);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_values", ctx, t, key);

            throw t;
        }
    }

    public Map<HK, HV> entries(K key) {

        Span span = Trace.startAsync("REDIS", "hash_entries");
        TraceContext ctx = Trace.currentContext();

        try {
            Map<HK, HV> o = this.hashOps.entries(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "hash_entries", ctx, null, key);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_entries", ctx, t, key);

            throw t;
        }
    }

    public Cursor<Map.Entry<HK, HV>> scan(K key, ScanOptions options) {

        Span span = Trace.startAsync("REDIS", "hash_scan");
        TraceContext ctx = Trace.currentContext();

        try {
            Cursor<Map.Entry<HK, HV>> o = this.hashOps.scan(key, options);
            span.stop(true);
            RedisLogFormat.log(o, span, "hash_scan", ctx, null, key, options);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "hash_scan", ctx, t, key, options);

            throw t;
        }
    }

    public RedisOperations<K, ?> getOperations() {
        return this.hashOps.getOperations();
    }
}
