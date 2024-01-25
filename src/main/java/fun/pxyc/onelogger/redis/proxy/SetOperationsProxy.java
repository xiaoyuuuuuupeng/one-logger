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
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;

public class SetOperationsProxy<K, V> implements SetOperations<K, V> {
    private final SetOperations<K, V> setOps;

    public SetOperationsProxy(SetOperations<K, V> setOps) {
        this.setOps = setOps;
    }

    public Long add(K key, V... values) {

        Span span = Trace.startAsync("REDIS", "set_add");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.setOps.add(key, values);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_add", ctx, null, key, values);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_add", ctx, t, key, values);
            throw t;
        }
    }

    public Long remove(K key, Object... values) {

        Span span = Trace.startAsync("REDIS", "set_remove");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.setOps.remove(key, values);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_remove", ctx, null, key, values);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_remove", ctx, t, key, values);

            throw t;
        }
    }

    public V pop(K key) {

        Span span = Trace.startAsync("REDIS", "set_pop");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.setOps.pop(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_pop", ctx, null, key);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_pop", ctx, t, key);

            throw t;
        }
    }

    public List<V> pop(K key, long count) {

        Span span = Trace.startAsync("REDIS", "set_pop");
        TraceContext ctx = Trace.currentContext();

        try {
            List<V> o = this.setOps.pop(key, count);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_pop", ctx, null, key, count);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_pop", ctx, t, key, count);

            throw t;
        }
    }

    public Boolean move(K key, V value, K destKey) {

        Span span = Trace.startAsync("REDIS", "set_move");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.setOps.move(key, value, destKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_move", ctx, null, key, value, destKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_move", ctx, t, key, value, destKey);

            throw t;
        }
    }

    public Long size(K key) {

        Span span = Trace.startAsync("REDIS", "set_size");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.setOps.size(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_size", ctx, null, key);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_size", ctx, t, key);

            throw t;
        }
    }

    public Boolean isMember(K key, Object o) {

        Span span = Trace.startAsync("REDIS", "set_isMember");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean bo = this.setOps.isMember(key, o);
            span.stop(true);
            RedisLogFormat.log(bo, span, "set_isMember", ctx, null, key, o);
            return bo;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_isMember", ctx, t, key, o);

            throw t;
        }
    }

    @Override
    public Map<Object, Boolean> isMember(K key, Object... objects) {
        Span span = Trace.startAsync("REDIS", "set_isMember");
        TraceContext ctx = Trace.currentContext();

        try {
            Map<Object, Boolean> bo = this.setOps.isMember(key, objects);
            span.stop(true);
            RedisLogFormat.log(bo, span, "set_isMember", ctx, null, key, objects);
            return bo;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_isMember", ctx, t, key, objects);

            throw t;
        }
    }

    public Set<V> intersect(K key, K otherKey) {

        Span span = Trace.startAsync("REDIS", "set_intersect");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.setOps.intersect(key, otherKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_intersect", ctx, null, key, otherKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_intersect", ctx, t, key, otherKey);

            throw t;
        }
    }

    public Set<V> intersect(K key, Collection<K> otherKeys) {

        Span span = Trace.startAsync("REDIS", "set_intersect");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.setOps.intersect(key, otherKeys);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_intersect", ctx, null, key, otherKeys);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_intersect", ctx, t, key, otherKeys);

            throw t;
        }
    }

    public Set<V> intersect(Collection<K> collection) {

        Span span = Trace.startAsync("REDIS", "set_intersect");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.setOps.intersect(collection);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_intersect", ctx, null, collection);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_intersect", ctx, t, collection);

            throw t;
        }
    }

    public Long intersectAndStore(K key, K otherKey, K destKey) {

        Span span = Trace.startAsync("REDIS", "set_intersectAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.setOps.intersectAndStore(key, otherKey, destKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_intersectAndStore", ctx, null, key, otherKey, destKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_intersectAndStore", ctx, t, key, otherKey, destKey);

            throw t;
        }
    }

    public Long intersectAndStore(K key, Collection<K> otherKeys, K destKey) {

        Span span = Trace.startAsync("REDIS", "set_intersectAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.setOps.intersectAndStore(key, otherKeys, destKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_intersectAndStore", ctx, null, key, otherKeys, destKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_intersectAndStore", ctx, t, key, otherKeys, destKey);

            throw t;
        }
    }

    public Long intersectAndStore(Collection<K> collection, K destKey) {

        Span span = Trace.startAsync("REDIS", "set_intersectAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.setOps.intersectAndStore(collection, destKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_intersectAndStore", ctx, null, collection, destKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_intersectAndStore", ctx, t, collection, destKey);

            throw t;
        }
    }

    public Set<V> union(K key, K otherKey) {

        Span span = Trace.startAsync("REDIS", "set_union");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.setOps.union(key, otherKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_union", ctx, null, key, otherKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_union", ctx, t, key, otherKey);

            throw t;
        }
    }

    public Set<V> union(K key, Collection<K> otherKeys) {

        Span span = Trace.startAsync("REDIS", "set_union");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.setOps.union(key, otherKeys);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_union", ctx, null, key, otherKeys);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_union", ctx, t, key, otherKeys);

            throw t;
        }
    }

    public Set<V> union(Collection<K> collection) {

        Span span = Trace.startAsync("REDIS", "set_union");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.setOps.union(collection);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_union", ctx, null, collection);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_union", ctx, t, collection);

            throw t;
        }
    }

    public Long unionAndStore(K key, K otherKey, K destKey) {

        Span span = Trace.startAsync("REDIS", "set_unionAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.setOps.unionAndStore(key, otherKey, destKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_unionAndStore", ctx, null, key, otherKey, destKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_unionAndStore", ctx, t, key, otherKey, destKey);

            throw t;
        }
    }

    public Long unionAndStore(K key, Collection<K> otherKeys, K destKey) {

        Span span = Trace.startAsync("REDIS", "set_unionAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.setOps.unionAndStore(key, otherKeys, destKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_unionAndStore", ctx, null, key, otherKeys, destKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_unionAndStore", ctx, t, key, otherKeys, destKey);

            throw t;
        }
    }

    public Long unionAndStore(Collection<K> collection, K destKey) {

        Span span = Trace.startAsync("REDIS", "set_unionAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.setOps.unionAndStore(collection, destKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_unionAndStore", ctx, null, collection, destKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_unionAndStore", ctx, t, collection, destKey);

            throw t;
        }
    }

    public Set<V> difference(K key, K otherKey) {

        Span span = Trace.startAsync("REDIS", "set_difference");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.setOps.difference(key, otherKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_difference", ctx, null, key, otherKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_difference", ctx, t, key, otherKey);

            throw t;
        }
    }

    public Set<V> difference(K key, Collection<K> otherKeys) {

        Span span = Trace.startAsync("REDIS", "set_difference");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.setOps.difference(key, otherKeys);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_difference", ctx, null, key, otherKeys);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_difference", ctx, t, key, otherKeys);

            throw t;
        }
    }

    public Set<V> difference(Collection<K> collection) {

        Span span = Trace.startAsync("REDIS", "set_difference");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.setOps.difference(collection);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_difference", ctx, null, collection);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_difference", ctx, t, collection);

            throw t;
        }
    }

    public Long differenceAndStore(K key, K otherKey, K destKey) {

        Span span = Trace.startAsync("REDIS", "set_differenceAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.setOps.differenceAndStore(key, otherKey, destKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_differenceAndStore", ctx, null, key, otherKey, destKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_differenceAndStore", ctx, t, key, otherKey, destKey);

            throw t;
        }
    }

    public Long differenceAndStore(K key, Collection<K> otherKeys, K destKey) {

        Span span = Trace.startAsync("REDIS", "set_differenceAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.setOps.differenceAndStore(key, otherKeys, destKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_differenceAndStore", ctx, null, key, otherKeys, destKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_differenceAndStore", ctx, t, key, otherKeys, destKey);

            throw t;
        }
    }

    public Long differenceAndStore(Collection<K> collection, K destKey) {

        Span span = Trace.startAsync("REDIS", "set_differenceAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.setOps.differenceAndStore(collection, destKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_differenceAndStore", ctx, null, collection, destKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_differenceAndStore", ctx, t, collection, destKey);

            throw t;
        }
    }

    public Set<V> members(K key) {

        Span span = Trace.startAsync("REDIS", "set_members");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.setOps.members(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_members", ctx, null, key);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_members", ctx, t, key);

            throw t;
        }
    }

    public V randomMember(K key) {

        Span span = Trace.startAsync("REDIS", "set_randomMember");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.setOps.randomMember(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_randomMember", ctx, null, key);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_randomMember", ctx, t, key);

            throw t;
        }
    }

    public Set<V> distinctRandomMembers(K key, long count) {

        Span span = Trace.startAsync("REDIS", "set_distinctRandomMembers");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.setOps.distinctRandomMembers(key, count);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_distinctRandomMembers", ctx, null, key, count);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_distinctRandomMembers", ctx, t, key, count);

            throw t;
        }
    }

    public List<V> randomMembers(K key, long count) {

        Span span = Trace.startAsync("REDIS", "set_randomMembers");
        TraceContext ctx = Trace.currentContext();

        try {
            List<V> o = this.setOps.randomMembers(key, count);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_randomMembers", ctx, null, key, count);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_randomMembers", ctx, t, key, count);

            throw t;
        }
    }

    public Cursor<V> scan(K key, ScanOptions options) {

        Span span = Trace.startAsync("REDIS", "set_scan");
        TraceContext ctx = Trace.currentContext();

        try {
            Cursor<V> o = this.setOps.scan(key, options);
            span.stop(true);
            RedisLogFormat.log(o, span, "set_scan", ctx, null, key, options);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "set_scan", ctx, t, key, options);

            throw t;
        }
    }

    public RedisOperations<K, V> getOperations() {
        return this.setOps.getOperations();
    }
}
