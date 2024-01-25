package fun.pxyc.onelogger.redis.proxy;

import fun.pxyc.onelogger.redis.RedisLogFormat;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ValueOperations;

public class ValueOperationsProxy<K, V> implements ValueOperations<K, V> {
    private final ValueOperations<K, V> valueOps;

    public ValueOperationsProxy(ValueOperations<K, V> valueOps) {
        this.valueOps = valueOps;
    }

    public void set(K key, V value) {

        Span span = Trace.startAsync("REDIS", "value_set");
        TraceContext ctx = Trace.currentContext();

        try {
            this.valueOps.set(key, value);
            span.stop(true);
            RedisLogFormat.log(null, span, "value_set", ctx, null, key, value);
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_set", ctx, throwable, key, value);
            throw throwable;
        }
    }

    public void set(K key, V value, long timeout, TimeUnit unit) {
        Span span = Trace.startAsync("REDIS", "value_set");
        TraceContext ctx = Trace.currentContext();

        try {
            this.valueOps.set(key, value, timeout, unit);
            span.stop(true);
            RedisLogFormat.log(null, span, "value_set", ctx, null, key, value, timeout, unit);
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_set", ctx, throwable, key, value, timeout, unit);
            throw throwable;
        }
    }

    public Boolean setIfAbsent(K key, V value) {
        Span span = Trace.startAsync("REDIS", "value_setIfAbsent");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.valueOps.setIfAbsent(key, value);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_setIfAbsent", ctx, null, key, value);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_setIfAbsent", ctx, throwable, key, value);
            throw throwable;
        }
    }

    public Boolean setIfAbsent(K key, V value, long l, TimeUnit timeUnit) {
        Span span = Trace.startAsync("REDIS", "value_setIfAbsent");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.valueOps.setIfAbsent(key, value, l, timeUnit);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_setIfAbsent", ctx, null, key, value, l, timeUnit);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_setIfAbsent", ctx, throwable, key, value, l, timeUnit);
            throw throwable;
        }
    }

    public Boolean setIfPresent(K key, V value) {
        Span span = Trace.startAsync("REDIS", "value_setIfPresent");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.valueOps.setIfPresent(key, value);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_setIfPresent", ctx, null, key, value);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_setIfPresent", ctx, throwable, key, value);
            throw throwable;
        }
    }

    public Boolean setIfPresent(K key, V value, long l, TimeUnit timeUnit) {
        Span span = Trace.startAsync("REDIS", "value_setIfPresent");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.valueOps.setIfPresent(key, value, l, timeUnit);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_setIfPresent", ctx, null, key, value, l, timeUnit);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_setIfPresent", ctx, throwable, key, value, l, timeUnit);
            throw throwable;
        }
    }

    public void multiSet(Map<? extends K, ? extends V> map) {
        Span span = Trace.startAsync("REDIS", "value_multiSet");
        TraceContext ctx = Trace.currentContext();

        try {
            this.valueOps.multiSet(map);
            span.stop(true);
            RedisLogFormat.log(null, span, "value_multiSet", ctx, null, map);
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_multiSet", ctx, throwable, map);
            throw throwable;
        }
    }

    public Boolean multiSetIfAbsent(Map<? extends K, ? extends V> map) {

        Span span = Trace.startAsync("REDIS", "value_multiSetIfAbsent");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.valueOps.multiSetIfAbsent(map);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_multiSetIfAbsent", ctx, null, map);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_multiSetIfAbsent", ctx, throwable, map);

            throw throwable;
        }
    }

    public V get(Object key) {
        Span span = Trace.startAsync("REDIS", "value_get");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.valueOps.get(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_get", ctx, null, key);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_get", ctx, throwable, key);

            throw throwable;
        }
    }

    @Override
    public V getAndDelete(K key) {
        Span span = Trace.startAsync("REDIS", "value_getAndDelete");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.valueOps.getAndDelete(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_getAndDelete", ctx, null, key);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_getAndDelete", ctx, throwable, key);

            throw throwable;
        }
    }

    @Override
    public V getAndExpire(K key, long timeout, TimeUnit unit) {
        Span span = Trace.startAsync("REDIS", "value_getAndExpire");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.valueOps.getAndExpire(key, timeout, unit);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_getAndExpire", ctx, null, key, timeout, unit);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_getAndExpire", ctx, throwable, key, timeout, unit);

            throw throwable;
        }
    }

    @Override
    public V getAndExpire(K key, Duration timeout) {
        Span span = Trace.startAsync("REDIS", "value_getAndExpire");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.valueOps.getAndExpire(key, timeout);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_getAndExpire", ctx, null, key, timeout);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_getAndExpire", ctx, throwable, key, timeout);

            throw throwable;
        }
    }

    @Override
    public V getAndPersist(K key) {
        Span span = Trace.startAsync("REDIS", "value_getAndPersist");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.valueOps.getAndPersist(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_getAndPersist", ctx, null, key);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_getAndPersist", ctx, throwable, key);

            throw throwable;
        }
    }

    public V getAndSet(K key, V value) {

        Span span = Trace.startAsync("REDIS", "value_getAndSet");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.valueOps.getAndSet(key, value);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_getAndSet", ctx, null, key, value);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_getAndSet", ctx, throwable, key, value);

            throw throwable;
        }
    }

    public List<V> multiGet(Collection<K> keys) {
        Span span = Trace.startAsync("REDIS", "value_multiGet");
        TraceContext ctx = Trace.currentContext();

        try {
            List<V> o = this.valueOps.multiGet(keys);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_multiGet", ctx, null, keys);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_multiGet", ctx, throwable, keys);

            throw throwable;
        }
    }

    public Long increment(K key) {

        Span span = Trace.startAsync("REDIS", "value_increment");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.valueOps.increment(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_increment", ctx, null, key);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_increment", ctx, throwable, key);

            throw throwable;
        }
    }

    public Long increment(K key, long delta) {

        Span span = Trace.startAsync("REDIS", "value_increment");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.valueOps.increment(key, delta);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_increment", ctx, null, key, delta);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_increment", ctx, throwable, key, delta);

            throw throwable;
        }
    }

    public Double increment(K key, double delta) {

        Span span = Trace.startAsync("REDIS", "value_increment");
        TraceContext ctx = Trace.currentContext();

        try {
            Double o = this.valueOps.increment(key, delta);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_increment", ctx, null, key, delta);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_increment", ctx, throwable, key, delta);

            throw throwable;
        }
    }

    public Long decrement(K key) {

        Span span = Trace.startAsync("REDIS", "value_decrement");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.valueOps.decrement(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_decrement", ctx, null, key);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_decrement", ctx, throwable, key);

            throw throwable;
        }
    }

    public Long decrement(K key, long delta) {

        Span span = Trace.startAsync("REDIS", "value_decrement");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.valueOps.decrement(key, delta);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_decrement", ctx, null, key, delta);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_decrement", ctx, throwable, key, delta);

            throw throwable;
        }
    }

    public Integer append(K key, String value) {

        Span span = Trace.startAsync("REDIS", "value_append");
        TraceContext ctx = Trace.currentContext();

        try {
            Integer o = this.valueOps.append(key, value);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_append", ctx, null, key, value);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_append", ctx, throwable, key, value);

            throw throwable;
        }
    }

    public String get(K key, long start, long end) {

        Span span = Trace.startAsync("REDIS", "value_get");
        TraceContext ctx = Trace.currentContext();

        try {
            String o = this.valueOps.get(key, start, end);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_get", ctx, null, key, start, end);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_get", ctx, throwable, key, start, end);

            throw throwable;
        }
    }

    public void set(K key, V value, long offset) {

        Span span = Trace.startAsync("REDIS", "value_set");
        TraceContext ctx = Trace.currentContext();

        try {
            this.valueOps.set(key, value, offset);
            span.stop(true);
            RedisLogFormat.log(null, span, "value_set", ctx, null, key, value, offset);
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_set", ctx, throwable, key, value, offset);

            throw throwable;
        }
    }

    public Long size(K key) {

        Span span = Trace.startAsync("REDIS", "value_size");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.valueOps.size(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_size", ctx, null, key);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_size", ctx, throwable, key);

            throw throwable;
        }
    }

    public Boolean setBit(K key, long offset, boolean value) {

        Span span = Trace.startAsync("REDIS", "value_setBit");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.valueOps.setBit(key, offset, value);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_setBit", ctx, null, key, offset, value);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_setBit", ctx, throwable, key, offset, value);

            throw throwable;
        }
    }

    public Boolean getBit(K key, long offset) {

        Span span = Trace.startAsync("REDIS", "value_getBit");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.valueOps.getBit(key, offset);
            span.stop(true);
            RedisLogFormat.log(o, span, "value_getBit", ctx, null, key, offset);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_getBit", ctx, throwable, key, offset);

            throw throwable;
        }
    }

    public List<Long> bitField(K key, BitFieldSubCommands bitFieldSubCommands) {

        Span span = Trace.startAsync("REDIS", "value_bitField");
        TraceContext ctx = Trace.currentContext();

        try {
            List<Long> ret = this.valueOps.bitField(key, bitFieldSubCommands);
            span.stop(true);
            RedisLogFormat.log(ret, span, "value_bitField", ctx, null, key, bitFieldSubCommands);
            return ret;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "value_bitField", ctx, throwable, key, bitFieldSubCommands);

            throw throwable;
        }
    }

    public RedisOperations<K, V> getOperations() {
        return this.valueOps.getOperations();
    }
}
