package fun.pxyc.onelogger.redis.proxy;

import fun.pxyc.onelogger.redis.RedisLogFormat;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;

public class BoundValueOperationsProxy<K, V> implements BoundValueOperations<K, V> {
    private final BoundValueOperations<K, V> boundValueOps;

    public BoundValueOperationsProxy(BoundValueOperations<K, V> boundValueOps) {
        this.boundValueOps = boundValueOps;
    }

    public void set(V value) {
        Span span = Trace.startAsync("REDIS", "boundValue_set");
        TraceContext ctx = Trace.currentContext();

        try {
            this.boundValueOps.set(value);
            span.stop(true);
            this.log(null, span, "boundValue_set", ctx, null, value);
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundValue_set", ctx, t, value);

            throw t;
        }
    }

    public void set(V value, long timeout, TimeUnit unit) {
        Span span = Trace.startAsync("REDIS", "boundValue_set");
        TraceContext ctx = Trace.currentContext();

        try {
            this.boundValueOps.set(value, timeout, unit);
            span.stop(true);
            this.log(null, span, "boundValue_set", ctx, null, value, timeout, unit);
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_set", ctx, throwable, value, timeout, unit);

            throw throwable;
        }
    }

    public Boolean setIfAbsent(V value) {
        Span span = Trace.startAsync("REDIS", "boundValue_setIfAbsent");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundValueOps.setIfAbsent(value);
            span.stop(true);
            this.log(o, span, "boundValue_setIfAbsent", ctx, null, value);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_setIfAbsent", ctx, throwable, value);

            throw throwable;
        }
    }

    public Boolean setIfAbsent(V value, long l, TimeUnit timeUnit) {
        Span span = Trace.startAsync("REDIS", "boundValue_setIfAbsent");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundValueOps.setIfAbsent(value, l, timeUnit);
            span.stop(true);
            this.log(o, span, "boundValue_setIfAbsent", ctx, null, value, l, timeUnit);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_setIfAbsent", ctx, throwable, value, l, timeUnit);

            throw throwable;
        }
    }

    public Boolean setIfPresent(V value) {
        Span span = Trace.startAsync("REDIS", "boundValue_setIfPresent");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundValueOps.setIfPresent(value);
            span.stop(true);
            this.log(o, span, "boundValue_setIfPresent", ctx, null, value);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_setIfPresent", ctx, throwable, value);

            throw throwable;
        }
    }

    public Boolean setIfPresent(V value, long l, TimeUnit timeUnit) {
        Span span = Trace.startAsync("REDIS", "boundValue_setIfPresent");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundValueOps.setIfPresent(value, l, timeUnit);
            span.stop(true);
            this.log(o, span, "boundValue_setIfPresent", ctx, null, value, l, timeUnit);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_setIfPresent", ctx, throwable, value, l, timeUnit);

            throw throwable;
        }
    }

    public V get() {
        Span span = Trace.startAsync("REDIS", "boundValue_get");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.boundValueOps.get();
            span.stop(true);
            this.log(o, span, "boundValue_get", ctx, null);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_get", ctx, throwable);

            throw throwable;
        }
    }

    @Override
    public V getAndDelete() {
        Span span = Trace.startAsync("REDIS", "boundValue_getAndDelete");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.boundValueOps.getAndDelete();
            span.stop(true);
            this.log(o, span, "boundValue_getAndDelete", ctx, null);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_getAndDelete", ctx, throwable);

            throw throwable;
        }
    }

    @Override
    public V getAndExpire(long timeout, TimeUnit unit) {
        Span span = Trace.startAsync("REDIS", "boundValue_getAndExpire");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.boundValueOps.getAndExpire(timeout, unit);
            span.stop(true);
            this.log(o, span, "boundValue_getAndExpire", ctx, null);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_getAndExpire", ctx, throwable);
            throw throwable;
        }
    }

    @Override
    public V getAndExpire(Duration timeout) {
        Span span = Trace.startAsync("REDIS", "boundValue_getAndExpire");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.boundValueOps.getAndExpire(timeout);
            span.stop(true);
            this.log(o, span, "boundValue_getAndExpire", ctx, null);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_getAndExpire", ctx, throwable);
            throw throwable;
        }
    }

    @Override
    public V getAndPersist() {
        Span span = Trace.startAsync("REDIS", "boundValue_getAndPersist");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.boundValueOps.getAndPersist();
            span.stop(true);
            this.log(o, span, "boundValue_getAndPersist", ctx, null);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_getAndPersist", ctx, throwable);
            throw throwable;
        }
    }

    public V getAndSet(V value) {
        Span span = Trace.startAsync("REDIS", "boundValue_getAndSet");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.boundValueOps.getAndSet(value);
            span.stop(true);
            this.log(o, span, "boundValue_getAndSet", ctx, null, value);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_getAndSet", ctx, throwable, value);

            throw throwable;
        }
    }

    public Long increment() {
        Span span = Trace.startAsync("REDIS", "boundValue_increment");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundValueOps.increment();
            span.stop(true);
            this.log(o, span, "boundValue_increment", ctx, null);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_increment", ctx, throwable);

            throw throwable;
        }
    }

    public Long increment(long delta) {
        Span span = Trace.startAsync("REDIS", "boundValue_increment");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundValueOps.increment(delta);
            span.stop(true);
            this.log(o, span, "boundValue_increment", ctx, null, delta);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_increment", ctx, throwable, delta);

            throw throwable;
        }
    }

    public Double increment(double delta) {
        Span span = Trace.startAsync("REDIS", "boundValue_increment");
        TraceContext ctx = Trace.currentContext();

        try {
            Double o = this.boundValueOps.increment(delta);
            span.stop(true);
            this.log(o, span, "boundValue_increment", ctx, null, delta);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_increment", ctx, throwable, delta);

            throw throwable;
        }
    }

    public Long decrement() {
        Span span = Trace.startAsync("REDIS", "boundValue_decrement");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundValueOps.decrement();
            span.stop(true);
            this.log(o, span, "boundValue_decrement", ctx, null);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_decrement", ctx, throwable);

            throw throwable;
        }
    }

    public Long decrement(long delta) {
        Span span = Trace.startAsync("REDIS", "boundValue_decrement");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundValueOps.decrement(delta);
            span.stop(true);
            this.log(o, span, "boundValue_decrement", ctx, null, delta);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_decrement", ctx, throwable, delta);

            throw throwable;
        }
    }

    public Integer append(String value) {
        Span span = Trace.startAsync("REDIS", "boundValue_append");
        TraceContext ctx = Trace.currentContext();

        try {
            Integer o = this.boundValueOps.append(value);
            span.stop(true);
            this.log(o, span, "boundValue_append", ctx, null, value);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_append", ctx, throwable, value);

            throw throwable;
        }
    }

    public String get(long start, long end) {
        Span span = Trace.startAsync("REDIS", "boundValue_get");
        TraceContext ctx = Trace.currentContext();

        try {
            String o = this.boundValueOps.get(start, end);
            span.stop(true);
            this.log(o, span, "boundValue_get", ctx, null, start, end);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_get", ctx, throwable, start, end);

            throw throwable;
        }
    }

    public void set(V value, long offset) {
        Span span = Trace.startAsync("REDIS", "boundValue_set");
        TraceContext ctx = Trace.currentContext();

        try {
            this.boundValueOps.set(value, offset);
            span.stop(true);
            this.log(null, span, "boundValue_set", ctx, null, value, offset);
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_set", ctx, throwable, value, offset);

            throw throwable;
        }
    }

    public Long size() {
        Span span = Trace.startAsync("REDIS", "boundValue_size");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundValueOps.size();
            span.stop(true);
            this.log(o, span, "boundValue_size", ctx, null);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_size", ctx, throwable);

            throw throwable;
        }
    }

    public RedisOperations<K, V> getOperations() {
        return this.boundValueOps.getOperations();
    }

    public K getKey() {
        return this.boundValueOps.getKey();
    }

    public DataType getType() {
        return this.boundValueOps.getType();
    }

    public Long getExpire() {
        Span span = Trace.startAsync("REDIS", "boundValue_getExpire");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundValueOps.getExpire();
            span.stop(true);
            this.log(o, span, "boundValue_getExpire", ctx, null);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_getExpire", ctx, throwable);

            throw throwable;
        }
    }

    public Boolean expire(long timeout, TimeUnit unit) {
        Span span = Trace.startAsync("REDIS", "boundValue_expire");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundValueOps.expire(timeout, unit);
            span.stop(true);
            this.log(o, span, "boundValue_expire", ctx, null, timeout, unit);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_expire", ctx, throwable, timeout, unit);

            throw throwable;
        }
    }

    public Boolean expireAt(Date date) {
        Span span = Trace.startAsync("REDIS", "boundValue_expireAt");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundValueOps.expireAt(date);
            span.stop(true);
            this.log(o, span, "boundValue_expireAt", ctx, null, date);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_expireAt", ctx, throwable, date);

            throw throwable;
        }
    }

    public Boolean persist() {
        Span span = Trace.startAsync("REDIS", "boundValue_persist");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundValueOps.persist();
            span.stop(true);
            this.log(o, span, "boundValue_persist", ctx, null);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_persist", ctx, throwable);

            throw throwable;
        }
    }

    public void rename(K newKey) {

        Span span = Trace.startAsync("REDIS", "boundValue_rename");
        TraceContext ctx = Trace.currentContext();

        try {
            this.boundValueOps.rename(newKey);
            span.stop(true);
            this.log(null, span, "boundValue_set", ctx, null, newKey);
        } catch (Throwable throwable) {
            span.stop(false);
            this.log(null, span, "boundValue_set", ctx, throwable, newKey);

            throw throwable;
        }
    }

    void log(Object res, Span span, String serviceName, TraceContext ctx, Throwable throwable, Object... param) {
        RedisLogFormat.log(res, span, serviceName, ctx, throwable, this.toArray(this.boundValueOps.getKey(), param));
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
