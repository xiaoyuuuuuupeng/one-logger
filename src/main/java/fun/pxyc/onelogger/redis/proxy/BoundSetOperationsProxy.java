package fun.pxyc.onelogger.redis.proxy;

import fun.pxyc.onelogger.redis.RedisLogFormat;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;

public class BoundSetOperationsProxy<K, V> implements BoundSetOperations<K, V> {
    private final BoundSetOperations<K, V> boundSetOps;

    public BoundSetOperationsProxy(BoundSetOperations<K, V> boundSetOps) {
        this.boundSetOps = boundSetOps;
    }

    public Long add(V... values) {
        Span span = Trace.startAsync("REDIS", "boundSet_add");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundSetOps.add(values);
            span.stop(true);
            this.log(o, span, "boundSet_add", ctx, null, values);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_add", ctx, t, values);

            throw t;
        }
    }

    public Long remove(Object... values) {
        Span span = Trace.startAsync("REDIS", "boundSet_remove");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundSetOps.remove(values);
            span.stop(true);
            this.log(o, span, "boundSet_remove", ctx, null, values);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_remove", ctx, t, values);

            throw t;
        }
    }

    public V pop() {
        Span span = Trace.startAsync("REDIS", "boundSet_pop");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.boundSetOps.pop();
            span.stop(true);
            this.log(o, span, "boundSet_pop", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_pop", ctx, t);

            throw t;
        }
    }

    public Boolean move(K destKey, V value) {
        Span span = Trace.startAsync("REDIS", "boundSet_move");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundSetOps.move(destKey, value);
            span.stop(true);
            this.log(o, span, "boundSet_move", ctx, null, destKey, value);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_move", ctx, t, destKey, value);

            throw t;
        }
    }

    public Long size() {
        Span span = Trace.startAsync("REDIS", "boundSet_size");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundSetOps.size();
            span.stop(true);
            this.log(o, span, "boundSet_size", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_size", ctx, t);

            throw t;
        }
    }

    public Boolean isMember(Object o) {
        Span span = Trace.startAsync("REDIS", "boundSet_isMember");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean oo = this.boundSetOps.isMember(o);
            span.stop(true);
            this.log(o, span, "boundSet_isMember", ctx, null, o);
            return oo;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_isMember", ctx, t, o);

            throw t;
        }
    }

    @Override
    public Map<Object, Boolean> isMember(Object... objects) {
        Span span = Trace.startAsync("REDIS", "boundSet_isMember");
        TraceContext ctx = Trace.currentContext();

        try {
            Map<Object, Boolean> oo = this.boundSetOps.isMember(objects);
            span.stop(true);
            this.log(oo, span, "boundSet_isMember", ctx, null, objects);
            return oo;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_isMember", ctx, t, objects);

            throw t;
        }
    }

    public Set<V> intersect(K key) {

        Span span = Trace.startAsync("REDIS", "boundSet_intersect");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.boundSetOps.intersect(key);
            span.stop(true);
            this.log(o, span, "boundSet_intersect", ctx, null, key);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_intersect", ctx, t, key);

            throw t;
        }
    }

    public Set<V> intersect(Collection<K> keys) {

        Span span = Trace.startAsync("REDIS", "boundSet_intersect");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.boundSetOps.intersect(keys);
            span.stop(true);
            this.log(o, span, "boundSet_intersect", ctx, null, keys);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_intersect", ctx, t, keys);

            throw t;
        }
    }

    public void intersectAndStore(K key, K destKey) {

        Span span = Trace.startAsync("REDIS", "boundSet_intersectAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            this.boundSetOps.intersectAndStore(key, destKey);
            span.stop(true);
            this.log(null, span, "boundSet_intersectAndStore", ctx, null, key, destKey);
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_intersectAndStore", ctx, t, key, destKey);

            throw t;
        }
    }

    public void intersectAndStore(Collection<K> keys, K destKey) {

        Span span = Trace.startAsync("REDIS", "boundSet_intersectAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            this.boundSetOps.intersectAndStore(keys, destKey);
            span.stop(true);
            this.log(null, span, "boundSet_intersectAndStore", ctx, null, keys, destKey);
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_intersectAndStore", ctx, t, keys, destKey);

            throw t;
        }
    }

    public Set<V> union(K key) {

        Span span = Trace.startAsync("REDIS", "boundSet_union");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.boundSetOps.union(key);
            span.stop(true);
            this.log(o, span, "boundSet_union", ctx, null, key);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_union", ctx, t, key);

            throw t;
        }
    }

    public Set<V> union(Collection<K> keys) {

        Span span = Trace.startAsync("REDIS", "boundSet_union");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.boundSetOps.union(keys);
            span.stop(true);
            this.log(o, span, "boundSet_union", ctx, null, keys);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_union", ctx, t, keys);

            throw t;
        }
    }

    public void unionAndStore(K key, K destKey) {

        Span span = Trace.startAsync("REDIS", "boundSet_unionAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            this.boundSetOps.unionAndStore(key, destKey);
            span.stop(true);
            this.log(null, span, "boundSet_unionAndStore", ctx, null, key, destKey);
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_unionAndStore", ctx, t, key, destKey);

            throw t;
        }
    }

    public void unionAndStore(Collection<K> keys, K destKey) {

        Span span = Trace.startAsync("REDIS", "boundSet_unionAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            this.boundSetOps.unionAndStore(keys, destKey);
            span.stop(true);
            this.log(null, span, "boundSet_unionAndStore", ctx, null, keys, destKey);
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_unionAndStore", ctx, t, keys, destKey);

            throw t;
        }
    }

    public Set<V> diff(K key) {

        Span span = Trace.startAsync("REDIS", "boundSet_diff");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.boundSetOps.diff(key);
            span.stop(true);
            this.log(o, span, "boundSet_diff", ctx, null, key);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_diff", ctx, t, key);

            throw t;
        }
    }

    public Set<V> diff(Collection<K> keys) {

        Span span = Trace.startAsync("REDIS", "boundSet_diff");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.boundSetOps.diff(keys);
            span.stop(true);
            this.log(o, span, "boundSet_diff", ctx, null, keys);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_diff", ctx, t, keys);

            throw t;
        }
    }

    public void diffAndStore(K key, K destKey) {

        Span span = Trace.startAsync("REDIS", "boundSet_diffAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            this.boundSetOps.diffAndStore(key, destKey);
            span.stop(true);
            this.log(null, span, "boundSet_diffAndStore", ctx, null, key, destKey);
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_diffAndStore", ctx, t, key, destKey);

            throw t;
        }
    }

    public void diffAndStore(Collection<K> keys, K destKey) {

        Span span = Trace.startAsync("REDIS", "boundSet_diffAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            this.boundSetOps.diffAndStore(keys, destKey);
            span.stop(true);
            this.log(null, span, "boundSet_diffAndStore", ctx, null, keys, destKey);
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_diffAndStore", ctx, t, keys, destKey);

            throw t;
        }
    }

    public Set<V> members() {
        Span span = Trace.startAsync("REDIS", "boundSet_members");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.boundSetOps.members();
            span.stop(true);
            this.log(o, span, "boundSet_members", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_members", ctx, t);

            throw t;
        }
    }

    public V randomMember() {
        Span span = Trace.startAsync("REDIS", "boundSet_randomMember");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.boundSetOps.randomMember();
            span.stop(true);
            this.log(o, span, "boundSet_randomMember", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_randomMember", ctx, t);

            throw t;
        }
    }

    public Set<V> distinctRandomMembers(long count) {
        Span span = Trace.startAsync("REDIS", "boundSet_distinctRandomMembers");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.boundSetOps.distinctRandomMembers(count);
            span.stop(true);
            this.log(o, span, "boundSet_distinctRandomMembers", ctx, null, count);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_distinctRandomMembers", ctx, t, count);

            throw t;
        }
    }

    public List<V> randomMembers(long count) {
        Span span = Trace.startAsync("REDIS", "boundSet_randomMembers");
        TraceContext ctx = Trace.currentContext();

        try {
            List<V> o = this.boundSetOps.randomMembers(count);
            span.stop(true);
            this.log(o, span, "boundSet_randomMembers", ctx, null, count);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_randomMembers", ctx, t, count);

            throw t;
        }
    }

    public Cursor<V> scan(ScanOptions options) {
        Span span = Trace.startAsync("REDIS", "boundSet_scan");
        TraceContext ctx = Trace.currentContext();

        try {
            Cursor<V> o = this.boundSetOps.scan(options);
            span.stop(true);
            this.log(o, span, "boundSet_scan", ctx, null, options);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_scan", ctx, t, options);

            throw t;
        }
    }

    public RedisOperations<K, V> getOperations() {
        return this.boundSetOps.getOperations();
    }

    public K getKey() {
        return this.boundSetOps.getKey();
    }

    public DataType getType() {
        return this.boundSetOps.getType();
    }

    public Long getExpire() {
        Span span = Trace.startAsync("REDIS", "boundSet_getExpire");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundSetOps.getExpire();
            span.stop(true);
            this.log(o, span, "boundSet_getExpire", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_getExpire", ctx, t);

            throw t;
        }
    }

    public Boolean expire(long timeout, TimeUnit unit) {
        Span span = Trace.startAsync("REDIS", "boundSet_expire");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundSetOps.expire(timeout, unit);
            span.stop(true);
            this.log(o, span, "boundSet_expire", ctx, null, timeout, unit);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_expire", ctx, t, timeout, unit);

            throw t;
        }
    }

    public Boolean expireAt(Date date) {
        Span span = Trace.startAsync("REDIS", "boundSet_expireAt");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundSetOps.expireAt(date);
            span.stop(true);
            this.log(o, span, "boundSet_expireAt", ctx, null, date);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_expireAt", ctx, t, date);

            throw t;
        }
    }

    public Boolean persist() {
        Span span = Trace.startAsync("REDIS", "boundSet_persist");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundSetOps.persist();
            span.stop(true);
            this.log(o, span, "boundSet_persist", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_persist", ctx, t);

            throw t;
        }
    }

    public void rename(K newKey) {

        Span span = Trace.startAsync("REDIS", "boundSet_rename");
        TraceContext ctx = Trace.currentContext();

        try {
            this.boundSetOps.rename(newKey);
            span.stop(true);
            this.log(null, span, "boundSet_rename", ctx, null, newKey);
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundSet_rename", ctx, t, newKey);

            throw t;
        }
    }

    void log(Object res, Span span, String serviceName, TraceContext ctx, Throwable throwable, Object... param) {
        RedisLogFormat.log(res, span, serviceName, ctx, throwable, this.toArray(this.boundSetOps.getKey(), param));
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
