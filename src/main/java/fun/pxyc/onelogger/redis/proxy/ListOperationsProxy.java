package fun.pxyc.onelogger.redis.proxy;

import fun.pxyc.onelogger.redis.RedisLogFormat;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;

public class ListOperationsProxy<K, V> implements ListOperations<K, V> {
    private final ListOperations<K, V> listOps;

    public ListOperationsProxy(ListOperations<K, V> listOps) {
        this.listOps = listOps;
    }

    public List<V> range(K key, long start, long end) {
        Span span = Trace.startAsync("REDIS", "list_range");
        TraceContext ctx = Trace.currentContext();

        try {
            List<V> o = this.listOps.range(key, start, end);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_range", ctx, null, key, start, end);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_range", ctx, throwable, key, start, end);
            throw throwable;
        }
    }

    public void trim(K key, long start, long end) {
        Span span = Trace.startAsync("REDIS", "list_trim");
        TraceContext ctx = Trace.currentContext();

        try {
            this.listOps.trim(key, start, end);
            span.stop(true);
            RedisLogFormat.log(null, span, "list_trim", ctx, null, key, start, end);
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_trim", ctx, throwable, key, start, end);
            throw throwable;
        }
    }

    public Long size(K key) {

        Span span = Trace.startAsync("REDIS", "list_size");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.listOps.size(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_size", ctx, null, key);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_size", ctx, throwable, key);

            throw throwable;
        }
    }

    public Long leftPush(K key, V value) {

        Span span = Trace.startAsync("REDIS", "list_leftPush");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.listOps.leftPush(key, value);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_leftPush", ctx, null, key, value);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_leftPush", ctx, throwable, key, value);

            throw throwable;
        }
    }

    public Long leftPushAll(K key, V... values) {

        Span span = Trace.startAsync("REDIS", "list_leftPushAll");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.listOps.leftPushAll(key, values);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_leftPushAll", ctx, null, key, values);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_leftPushAll", ctx, throwable, key, values);

            throw throwable;
        }
    }

    public Long leftPushAll(K key, Collection<V> values) {

        Span span = Trace.startAsync("REDIS", "list_leftPushAll");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.listOps.leftPushAll(key, values);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_leftPushAll", ctx, null, key, values);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_leftPushAll", ctx, throwable, key, values);

            throw throwable;
        }
    }

    public Long leftPushIfPresent(K key, V value) {

        Span span = Trace.startAsync("REDIS", "list_leftPushIfPresent");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.listOps.leftPushIfPresent(key, value);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_leftPushIfPresent", ctx, null, key, value);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_leftPushIfPresent", ctx, throwable, key, value);

            throw throwable;
        }
    }

    public Long leftPush(K key, V pivot, V value) {

        Span span = Trace.startAsync("REDIS", "list_leftPush");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.listOps.leftPush(key, pivot, value);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_leftPush", ctx, null, key, pivot, value);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_leftPush", ctx, throwable, key, pivot, value);

            throw throwable;
        }
    }

    public Long rightPush(K key, V value) {

        Span span = Trace.startAsync("REDIS", "list_rightPush");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.listOps.rightPush(key, value);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_rightPush", ctx, null, key, value);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_rightPush", ctx, throwable, key, value);

            throw throwable;
        }
    }

    public Long rightPushAll(K key, V... values) {

        Span span = Trace.startAsync("REDIS", "list_rightPushAll");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.listOps.rightPushAll(key, values);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_rightPushAll", ctx, null, key, values);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_rightPushAll", ctx, throwable, key, values);

            throw throwable;
        }
    }

    public Long rightPushAll(K key, Collection<V> values) {

        Span span = Trace.startAsync("REDIS", "list_rightPushAll");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.listOps.rightPushAll(key, values);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_rightPushAll", ctx, null, key, values);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_rightPushAll", ctx, throwable, key, values);

            throw throwable;
        }
    }

    public Long rightPushIfPresent(K key, V value) {

        Span span = Trace.startAsync("REDIS", "list_rightPushIfPresent");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.listOps.rightPushIfPresent(key, value);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_rightPushIfPresent", ctx, null, key, value);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_rightPushIfPresent", ctx, throwable, key, value);

            throw throwable;
        }
    }

    public Long rightPush(K key, V pivot, V value) {

        Span span = Trace.startAsync("REDIS", "list_rightPush");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.listOps.rightPush(key, pivot, value);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_rightPush", ctx, null, key, pivot, value);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_rightPush", ctx, throwable, key, pivot, value);

            throw throwable;
        }
    }

    @Override
    public V move(K sourceKey, RedisListCommands.Direction from, K destinationKey, RedisListCommands.Direction to) {
        Span span = Trace.startAsync("REDIS", "list_move");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.listOps.move(sourceKey, from, destinationKey, to);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_move", ctx, null, from, destinationKey, to);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_move", ctx, throwable, from, destinationKey, to);
            throw throwable;
        }
    }

    @Override
    public V move(
            K sourceKey,
            RedisListCommands.Direction from,
            K destinationKey,
            RedisListCommands.Direction to,
            long timeout,
            TimeUnit unit) {
        Span span = Trace.startAsync("REDIS", "list_move");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.listOps.move(sourceKey, from, destinationKey, to, timeout, unit);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_move", ctx, null, from, destinationKey, to, timeout, unit);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_move", ctx, throwable, from, destinationKey, to, timeout, unit);
            throw throwable;
        }
    }

    public void set(K key, long index, V value) {

        Span span = Trace.startAsync("REDIS", "list_set");
        TraceContext ctx = Trace.currentContext();

        try {
            this.listOps.set(key, index, value);
            span.stop(true);
            RedisLogFormat.log(null, span, "list_set", ctx, null, key, index, value);
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_set", ctx, throwable, key, index, value);

            throw throwable;
        }
    }

    public Long remove(K key, long count, Object value) {

        Span span = Trace.startAsync("REDIS", "list_remove");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.listOps.remove(key, count, value);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_remove", ctx, null, key, count, value);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_remove", ctx, throwable, key, count, value);

            throw throwable;
        }
    }

    public V index(K key, long index) {

        Span span = Trace.startAsync("REDIS", "list_index");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.listOps.index(key, index);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_index", ctx, null, key, index);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_index", ctx, throwable, key, index);

            throw throwable;
        }
    }

    @Override
    public Long indexOf(K key, V value) {
        return null;
    }

    @Override
    public Long lastIndexOf(K key, V value) {
        return null;
    }

    public V leftPop(K key) {

        Span span = Trace.startAsync("REDIS", "list_leftPop");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.listOps.leftPop(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_leftPop", ctx, null, key);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_leftPop", ctx, throwable, key);

            throw throwable;
        }
    }

    @Override
    public List<V> leftPop(K key, long count) {
        return null;
    }

    public V leftPop(K key, long timeout, TimeUnit unit) {

        Span span = Trace.startAsync("REDIS", "list_leftPop");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.listOps.leftPop(key, timeout, unit);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_leftPop", ctx, null, key, timeout, unit);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_leftPop", ctx, throwable, key, timeout, unit);

            throw throwable;
        }
    }

    public V rightPop(K key) {

        Span span = Trace.startAsync("REDIS", "list_rightPop");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.listOps.rightPop(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_rightPop", ctx, null, key);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_rightPop", ctx, throwable, key);

            throw throwable;
        }
    }

    @Override
    public List<V> rightPop(K key, long count) {
        return null;
    }

    public V rightPop(K key, long timeout, TimeUnit unit) {

        Span span = Trace.startAsync("REDIS", "list_rightPop");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.listOps.rightPop(key, timeout, unit);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_rightPop", ctx, null, key, timeout, unit);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_rightPop", ctx, throwable, key, timeout, unit);

            throw throwable;
        }
    }

    public V rightPopAndLeftPush(K sourceKey, K destinationKey) {
        Span span = Trace.startAsync("REDIS", "list_rightPopAndLeftPush");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.listOps.rightPopAndLeftPush(sourceKey, destinationKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "list_rightPopAndLeftPush", ctx, null, sourceKey, destinationKey);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(null, span, "list_rightPopAndLeftPush", ctx, throwable, sourceKey, destinationKey);

            throw throwable;
        }
    }

    public V rightPopAndLeftPush(K sourceKey, K destinationKey, long timeout, TimeUnit unit) {
        Span span = Trace.startAsync("REDIS", "list_rightPopAndLeftPush");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.listOps.rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit);
            span.stop(true);
            RedisLogFormat.log(
                    o, span, "list_rightPopAndLeftPush", ctx, null, sourceKey, destinationKey, timeout, unit);
            return o;
        } catch (Throwable throwable) {
            span.stop(false);
            RedisLogFormat.log(
                    null, span, "list_rightPopAndLeftPush", ctx, throwable, sourceKey, destinationKey, timeout, unit);

            throw throwable;
        }
    }

    public RedisOperations<K, V> getOperations() {
        return this.listOps.getOperations();
    }
}
