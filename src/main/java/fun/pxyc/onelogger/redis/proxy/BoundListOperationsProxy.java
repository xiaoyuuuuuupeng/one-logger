package fun.pxyc.onelogger.redis.proxy;

import fun.pxyc.onelogger.redis.RedisLogFormat;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisOperations;

public class BoundListOperationsProxy<K, V> implements BoundListOperations<K, V> {
    private final BoundListOperations<K, V> boundListOps;

    public BoundListOperationsProxy(BoundListOperations<K, V> boundListOps) {
        this.boundListOps = boundListOps;
    }

    public List<V> range(long start, long end) {
        Span span = Trace.startAsync("REDIS", "boundList_range");
        TraceContext ctx = Trace.currentContext();

        try {
            List<V> o = this.boundListOps.range(start, end);
            span.stop(true);
            this.log(o, span, "boundList_range", ctx, null, start, end);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_range", ctx, t, start, end);

            throw t;
        }
    }

    public void trim(long start, long end) {
        Span span = Trace.startAsync("REDIS", "boundList_trim");
        TraceContext ctx = Trace.currentContext();

        try {
            this.boundListOps.trim(start, end);
            span.stop(true);
            this.log(null, span, "boundList_trim", ctx, null, start, end);
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_trim", ctx, t, start, end);

            throw t;
        }
    }

    public Long size() {
        Span span = Trace.startAsync("REDIS", "boundList_size");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundListOps.size();
            span.stop(true);
            this.log(o, span, "boundList_size", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_size", ctx, t);

            throw t;
        }
    }

    public Long leftPush(V value) {
        Span span = Trace.startAsync("REDIS", "boundList_leftPush");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundListOps.leftPush(value);
            span.stop(true);
            this.log(o, span, "boundList_leftPush", ctx, null, value);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_leftPush", ctx, t, value);

            throw t;
        }
    }

    public Long leftPushAll(V... values) {
        Span span = Trace.startAsync("REDIS", "boundList_leftPushAll");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundListOps.leftPushAll(values);
            span.stop(true);
            this.log(o, span, "boundList_leftPushAll", ctx, null, values);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_leftPushAll", ctx, t, values);

            throw t;
        }
    }

    public Long leftPushIfPresent(V value) {
        Span span = Trace.startAsync("REDIS", "boundList_leftPushIfPresent");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundListOps.leftPushIfPresent(value);
            span.stop(true);
            this.log(o, span, "boundList_leftPushIfPresent", ctx, null, value);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_leftPushIfPresent", ctx, t, value);

            throw t;
        }
    }

    public Long leftPush(V pivot, V value) {
        Span span = Trace.startAsync("REDIS", "boundList_leftPush");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundListOps.leftPush(pivot, value);
            span.stop(true);
            this.log(o, span, "boundList_leftPush", ctx, null, pivot, value);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_leftPush", ctx, t, pivot, value);

            throw t;
        }
    }

    public Long rightPush(V value) {
        Span span = Trace.startAsync("REDIS", "boundList_rightPush");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundListOps.rightPush(value);
            span.stop(true);
            this.log(o, span, "boundList_rightPush", ctx, null, value);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_rightPush", ctx, t, value);

            throw t;
        }
    }

    public Long rightPushAll(V... values) {
        Span span = Trace.startAsync("REDIS", "boundList_rightPushAll");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundListOps.rightPushAll(values);
            span.stop(true);
            this.log(o, span, "boundList_rightPushAll", ctx, null, values);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_rightPushAll", ctx, t, values);

            throw t;
        }
    }

    public Long rightPushIfPresent(V value) {
        Span span = Trace.startAsync("REDIS", "boundList_rightPushIfPresent");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundListOps.rightPushIfPresent(value);
            span.stop(true);
            this.log(o, span, "boundList_rightPushIfPresent", ctx, null, value);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_rightPushIfPresent", ctx, t, value);

            throw t;
        }
    }

    public Long rightPush(V pivot, V value) {
        Span span = Trace.startAsync("REDIS", "boundList_rightPush");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundListOps.rightPush(pivot, value);
            span.stop(true);
            this.log(o, span, "boundList_rightPush", ctx, null, pivot, value);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_rightPush", ctx, t, pivot, value);

            throw t;
        }
    }

    @Override
    public V move(RedisListCommands.Direction from, K destinationKey, RedisListCommands.Direction to) {
        Span span = Trace.startAsync("REDIS", "boundList_move");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.boundListOps.move(from, destinationKey, to);
            span.stop(true);
            this.log(o, span, "boundList_move", ctx, null, from, destinationKey, to);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_move", ctx, t, from, destinationKey, to);

            throw t;
        }
    }

    @Override
    public V move(
            RedisListCommands.Direction from, K destinationKey, RedisListCommands.Direction to, Duration timeout) {
        Span span = Trace.startAsync("REDIS", "boundList_move");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.boundListOps.move(from, destinationKey, to, timeout);
            span.stop(true);
            this.log(o, span, "boundList_move", ctx, null, from, destinationKey, to, timeout);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_move", ctx, t, from, destinationKey, to, timeout);

            throw t;
        }
    }

    @Override
    public V move(
            RedisListCommands.Direction from,
            K destinationKey,
            RedisListCommands.Direction to,
            long timeout,
            TimeUnit unit) {
        Span span = Trace.startAsync("REDIS", "boundList_move");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.boundListOps.move(from, destinationKey, to, timeout, unit);
            span.stop(true);
            this.log(o, span, "boundList_move", ctx, null, from, destinationKey, to, timeout, unit);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_move", ctx, t, from, destinationKey, to, timeout, unit);
            throw t;
        }
    }

    public void set(long index, V value) {
        Span span = Trace.startAsync("REDIS", "boundList_set");
        TraceContext ctx = Trace.currentContext();

        try {
            this.boundListOps.set(index, value);
            span.stop(true);
            this.log(null, span, "boundList_set", ctx, null, index, value);
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_set", ctx, t, index, value);

            throw t;
        }
    }

    public Long remove(long count, Object value) {
        Span span = Trace.startAsync("REDIS", "boundList_remove");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundListOps.remove(count, value);
            span.stop(true);
            this.log(o, span, "boundList_remove", ctx, null, count, value);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_remove", ctx, t, count, value);

            throw t;
        }
    }

    public V index(long index) {
        Span span = Trace.startAsync("REDIS", "boundList_index");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.boundListOps.index(index);
            span.stop(true);
            this.log(o, span, "boundList_index", ctx, null, index);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_index", ctx, t, index);

            throw t;
        }
    }

    @Override
    public Long indexOf(V value) {
        Span span = Trace.startAsync("REDIS", "boundList_indexOf");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundListOps.indexOf(value);
            span.stop(true);
            this.log(o, span, "boundList_indexOf", ctx, null, value);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_indexOf", ctx, t, value);

            throw t;
        }
    }

    @Override
    public Long lastIndexOf(V value) {
        Span span = Trace.startAsync("REDIS", "boundList_lastIndexOf");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundListOps.lastIndexOf(value);
            span.stop(true);
            this.log(o, span, "boundList_lastIndexOf", ctx, null, value);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_lastIndexOf", ctx, t, value);

            throw t;
        }
    }

    public V leftPop() {
        Span span = Trace.startAsync("REDIS", "boundList_leftPop");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.boundListOps.leftPop();
            span.stop(true);
            this.log(o, span, "boundList_leftPop", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_leftPop", ctx, t);

            throw t;
        }
    }

    @Override
    public List<V> leftPop(long count) {
        return null;
    }

    public V leftPop(long timeout, TimeUnit unit) {
        Span span = Trace.startAsync("REDIS", "boundList_leftPop");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.boundListOps.leftPop(timeout, unit);
            span.stop(true);
            this.log(o, span, "boundList_leftPop", ctx, null, timeout, unit);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_leftPop", ctx, t, timeout, unit);

            throw t;
        }
    }

    public V rightPop() {
        Span span = Trace.startAsync("REDIS", "boundList_rightPop");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.boundListOps.rightPop();
            span.stop(true);
            this.log(o, span, "boundList_rightPop", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_rightPop", ctx, t);

            throw t;
        }
    }

    @Override
    public List<V> rightPop(long count) {
        Span span = Trace.startAsync("REDIS", "boundList_rightPop");
        TraceContext ctx = Trace.currentContext();

        try {
            List<V> o = this.boundListOps.rightPop(count);
            span.stop(true);
            this.log(o, span, "boundList_rightPop", ctx, null, count);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_rightPop", ctx, t, count);

            throw t;
        }
    }

    public V rightPop(long timeout, TimeUnit unit) {
        Span span = Trace.startAsync("REDIS", "boundList_rightPop");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.boundListOps.rightPop(timeout, unit);
            span.stop(true);
            this.log(o, span, "boundList_rightPop", ctx, null, timeout, unit);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_rightPop", ctx, t, timeout, unit);

            throw t;
        }
    }

    public RedisOperations<K, V> getOperations() {
        return this.boundListOps.getOperations();
    }

    public K getKey() {
        return this.boundListOps.getKey();
    }

    public DataType getType() {
        return this.boundListOps.getType();
    }

    public Long getExpire() {
        Span span = Trace.startAsync("REDIS", "boundList_getExpire");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundListOps.getExpire();
            span.stop(true);
            this.log(o, span, "boundList_getExpire", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_getExpire", ctx, t);

            throw t;
        }
    }

    public Boolean expire(long timeout, TimeUnit unit) {
        Span span = Trace.startAsync("REDIS", "boundList_expire");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundListOps.expire(timeout, unit);
            span.stop(true);
            this.log(o, span, "boundList_expire", ctx, null, timeout, unit);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_expire", ctx, t, timeout, unit);

            throw t;
        }
    }

    public Boolean expireAt(Date date) {
        Span span = Trace.startAsync("REDIS", "boundList_expireAt");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundListOps.expireAt(date);
            span.stop(true);
            this.log(o, span, "boundList_expireAt", ctx, null, date);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_expireAt", ctx, t, date);

            throw t;
        }
    }

    public Boolean persist() {
        Span span = Trace.startAsync("REDIS", "boundList_persist");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundListOps.persist();
            span.stop(true);
            this.log(o, span, "boundList_persist", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_persist", ctx, t);

            throw t;
        }
    }

    public void rename(K newKey) {

        Span span = Trace.startAsync("REDIS", "boundList_rename");
        TraceContext ctx = Trace.currentContext();

        try {
            this.boundListOps.rename(newKey);
            span.stop(true);
            this.log(null, span, "boundList_rename", ctx, null, newKey);
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundList_rename", ctx, t, newKey);

            throw t;
        }
    }

    void log(Object res, Span span, String serviceName, TraceContext ctx, Throwable throwable, Object... param) {
        RedisLogFormat.log(res, span, serviceName, ctx, throwable, this.toArray(this.boundListOps.getKey(), param));
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
