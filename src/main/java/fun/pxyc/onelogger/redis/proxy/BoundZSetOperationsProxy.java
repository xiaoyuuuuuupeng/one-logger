package fun.pxyc.onelogger.redis.proxy;

import fun.pxyc.onelogger.redis.RedisLogFormat;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;

public class BoundZSetOperationsProxy<K, V> implements BoundZSetOperations<K, V> {
    private final BoundZSetOperations<K, V> boundZSetOps;

    public BoundZSetOperationsProxy(BoundZSetOperations<K, V> boundZSetOps) {
        this.boundZSetOps = boundZSetOps;
    }

    public Boolean add(V value, double score) {
        Span span = Trace.startAsync("REDIS", "boundZSet_add");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundZSetOps.add(value, score);
            span.stop(true);
            this.log(o, span, "boundZSet_add", ctx, null, value, score);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_add", ctx, t, value, score);

            throw t;
        }
    }

    @Override
    public Boolean addIfAbsent(V value, double score) {
        Span span = Trace.startAsync("REDIS", "boundZSet_addIfAbsent");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundZSetOps.addIfAbsent(value, score);
            span.stop(true);
            this.log(o, span, "boundZSet_addIfAbsent", ctx, null, value, score);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_addIfAbsent", ctx, t, value, score);

            throw t;
        }
    }

    public Long add(Set<ZSetOperations.TypedTuple<V>> typedTuples) {
        Span span = Trace.startAsync("REDIS", "boundZSet_add");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundZSetOps.add(typedTuples);
            span.stop(true);
            this.log(o, span, "boundZSet_add", ctx, null, typedTuples);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_add", ctx, t, typedTuples);

            throw t;
        }
    }

    @Override
    public Long addIfAbsent(Set<ZSetOperations.TypedTuple<V>> typedTuples) {
        Span span = Trace.startAsync("REDIS", "boundZSet_addIfAbsent");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundZSetOps.addIfAbsent(typedTuples);
            span.stop(true);
            this.log(o, span, "boundZSet_addIfAbsent", ctx, null, typedTuples);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_addIfAbsent", ctx, t, typedTuples);

            throw t;
        }
    }

    public Long remove(Object... values) {
        Span span = Trace.startAsync("REDIS", "boundZSet_remove");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundZSetOps.remove(values);
            span.stop(true);
            this.log(o, span, "boundZSet_remove", ctx, null, values);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_remove", ctx, t, values);

            throw t;
        }
    }

    public Double incrementScore(V value, double delta) {
        Span span = Trace.startAsync("REDIS", "boundZSet_incrementScore");
        TraceContext ctx = Trace.currentContext();

        try {
            Double o = this.boundZSetOps.incrementScore(value, delta);
            span.stop(true);
            this.log(o, span, "boundZSet_incrementScore", ctx, null, value, delta);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_incrementScore", ctx, t, value, delta);

            throw t;
        }
    }

    @Override
    public V randomMember() {
        Span span = Trace.startAsync("REDIS", "boundZSet_randomMember");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.boundZSetOps.randomMember();
            span.stop(true);
            this.log(o, span, "boundZSet_randomMember", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_incrementScore", ctx, t);

            throw t;
        }
    }

    @Override
    public Set<V> distinctRandomMembers(long count) {
        return null;
    }

    @Override
    public List<V> randomMembers(long count) {
        return null;
    }

    @Override
    public ZSetOperations.TypedTuple<V> randomMemberWithScore() {
        return null;
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> distinctRandomMembersWithScore(long count) {
        return null;
    }

    @Override
    public List<ZSetOperations.TypedTuple<V>> randomMembersWithScore(long count) {
        return null;
    }

    public Long rank(Object o) {
        Span span = Trace.startAsync("REDIS", "boundZSet_rank");
        TraceContext ctx = Trace.currentContext();

        try {
            Long lo = this.boundZSetOps.rank(o);
            span.stop(true);
            this.log(lo, span, "boundZSet_rank", ctx, null, o);
            return lo;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_rank", ctx, t, o);

            throw t;
        }
    }

    public Long reverseRank(Object o) {
        Span span = Trace.startAsync("REDIS", "boundZSet_reverseRank");
        TraceContext ctx = Trace.currentContext();

        try {
            Long lo = this.boundZSetOps.reverseRank(o);
            span.stop(true);
            this.log(lo, span, "boundZSet_reverseRank", ctx, null, o);
            return lo;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_reverseRank", ctx, t, o);

            throw t;
        }
    }

    public Set<V> range(long start, long end) {
        Span span = Trace.startAsync("REDIS", "boundZSet_range");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.boundZSetOps.range(start, end);
            span.stop(true);
            this.log(o, span, "boundZSet_range", ctx, null, start, end);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_range", ctx, t, start, end);

            throw t;
        }
    }

    public Set<ZSetOperations.TypedTuple<V>> rangeWithScores(long start, long end) {
        Span span = Trace.startAsync("REDIS", "boundZSet_rangeWithScores");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<ZSetOperations.TypedTuple<V>> o = this.boundZSetOps.rangeWithScores(start, end);
            span.stop(true);
            this.log(o, span, "boundZSet_rangeWithScores", ctx, null, start, end);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_rangeWithScores", ctx, t, start, end);

            throw t;
        }
    }

    public Set<V> rangeByScore(double min, double max) {
        Span span = Trace.startAsync("REDIS", "boundZSet_rangeByScore");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.boundZSetOps.rangeByScore(min, max);
            span.stop(true);
            this.log(o, span, "boundZSet_rangeByScore", ctx, null, min, max);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_rangeByScore", ctx, t, min, max);

            throw t;
        }
    }

    public Set<ZSetOperations.TypedTuple<V>> rangeByScoreWithScores(double min, double max) {
        Span span = Trace.startAsync("REDIS", "boundZSet_rangeByScoreWithScores");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<ZSetOperations.TypedTuple<V>> o = this.boundZSetOps.rangeByScoreWithScores(min, max);
            span.stop(true);
            this.log(o, span, "boundZSet_rangeByScoreWithScores", ctx, null, min, max);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_rangeByScoreWithScores", ctx, t, min, max);

            throw t;
        }
    }

    public Set<V> reverseRange(long start, long end) {
        Span span = Trace.startAsync("REDIS", "boundZSet_reverseRange");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.boundZSetOps.reverseRange(start, end);
            span.stop(true);
            this.log(o, span, "boundZSet_reverseRange", ctx, null, start, end);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_reverseRange", ctx, t, start, end);

            throw t;
        }
    }

    public Set<ZSetOperations.TypedTuple<V>> reverseRangeWithScores(long start, long end) {
        Span span = Trace.startAsync("REDIS", "boundZSet_reverseRangeWithScores");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<ZSetOperations.TypedTuple<V>> o = this.boundZSetOps.reverseRangeWithScores(start, end);
            span.stop(true);
            this.log(o, span, "boundZSet_reverseRangeWithScores", ctx, null, start, end);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_reverseRangeWithScores", ctx, t, start, end);

            throw t;
        }
    }

    public Set<V> reverseRangeByScore(double min, double max) {
        Span span = Trace.startAsync("REDIS", "boundZSet_reverseRangeByScore");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.boundZSetOps.reverseRangeByScore(min, max);
            span.stop(true);
            this.log(o, span, "boundZSet_reverseRangeByScore", ctx, null, min, max);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_reverseRangeByScore", ctx, t, min, max);

            throw t;
        }
    }

    public Set<ZSetOperations.TypedTuple<V>> reverseRangeByScoreWithScores(double min, double max) {
        Span span = Trace.startAsync("REDIS", "boundZSet_reverseRangeByScoreWithScores");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<ZSetOperations.TypedTuple<V>> o = this.boundZSetOps.reverseRangeByScoreWithScores(min, max);
            span.stop(true);
            this.log(o, span, "boundZSet_reverseRangeByScoreWithScores", ctx, null, min, max);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_reverseRangeByScoreWithScores", ctx, t, min, max);

            throw t;
        }
    }

    public Long count(double min, double max) {
        Span span = Trace.startAsync("REDIS", "boundZSet_count");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundZSetOps.count(min, max);
            span.stop(true);
            this.log(o, span, "boundZSet_count", ctx, null, min, max);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_count", ctx, t, min, max);

            throw t;
        }
    }

    @Override
    public Long lexCount(RedisZSetCommands.Range range) {
        Span span = Trace.startAsync("REDIS", "boundZSet_lexCount");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundZSetOps.lexCount(range);
            span.stop(true);
            this.log(o, span, "boundZSet_lexCount", ctx, null, range);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_lexCount", ctx, t, range);

            throw t;
        }
    }

    @Override
    public ZSetOperations.TypedTuple<V> popMin() {
        Span span = Trace.startAsync("REDIS", "boundZSet_popMin");
        TraceContext ctx = Trace.currentContext();

        try {
            ZSetOperations.TypedTuple<V> o = this.boundZSetOps.popMin();
            span.stop(true);
            this.log(o, span, "boundZSet_popMin", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_popMin", ctx, t);
            throw t;
        }
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> popMin(long count) {
        Span span = Trace.startAsync("REDIS", "boundZSet_popMin");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<ZSetOperations.TypedTuple<V>> o = this.boundZSetOps.popMin(count);
            span.stop(true);
            this.log(o, span, "boundZSet_popMin", ctx, null, count);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_popMin", ctx, t, count);
            throw t;
        }
    }

    @Override
    public ZSetOperations.TypedTuple<V> popMin(long timeout, TimeUnit unit) {
        Span span = Trace.startAsync("REDIS", "boundZSet_popMin");
        TraceContext ctx = Trace.currentContext();

        try {
            ZSetOperations.TypedTuple<V> o = this.boundZSetOps.popMin(timeout, unit);
            span.stop(true);
            this.log(o, span, "boundZSet_popMin", ctx, null, timeout, unit);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_popMin", ctx, t, timeout, unit);
            throw t;
        }
    }

    @Override
    public ZSetOperations.TypedTuple<V> popMax() {
        Span span = Trace.startAsync("REDIS", "boundZSet_popMax");
        TraceContext ctx = Trace.currentContext();

        try {
            ZSetOperations.TypedTuple<V> o = this.boundZSetOps.popMax();
            span.stop(true);
            this.log(o, span, "boundZSet_popMax", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_popMax", ctx, t);
            throw t;
        }
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> popMax(long count) {
        Span span = Trace.startAsync("REDIS", "boundZSet_popMax");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<ZSetOperations.TypedTuple<V>> o = this.boundZSetOps.popMax(count);
            span.stop(true);
            this.log(o, span, "boundZSet_popMax", ctx, null, count);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_popMax", ctx, t, count);
            throw t;
        }
    }

    @Override
    public ZSetOperations.TypedTuple<V> popMax(long timeout, TimeUnit unit) {
        Span span = Trace.startAsync("REDIS", "boundZSet_popMax");
        TraceContext ctx = Trace.currentContext();

        try {
            ZSetOperations.TypedTuple<V> o = this.boundZSetOps.popMax(timeout, unit);
            span.stop(true);
            this.log(o, span, "boundZSet_popMax", ctx, null, timeout, unit);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_popMax", ctx, t, timeout, unit);
            throw t;
        }
    }

    public Long size() {
        Span span = Trace.startAsync("REDIS", "boundZSet_size");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundZSetOps.size();
            span.stop(true);
            this.log(o, span, "boundZSet_size", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_size", ctx, t);

            throw t;
        }
    }

    public Long zCard() {
        Span span = Trace.startAsync("REDIS", "boundZSet_zCard");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundZSetOps.zCard();
            span.stop(true);
            this.log(o, span, "boundZSet_zCard", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_zCard", ctx, t);

            throw t;
        }
    }

    public Double score(Object o) {
        Span span = Trace.startAsync("REDIS", "boundZSet_score");
        TraceContext ctx = Trace.currentContext();

        try {
            Double ou = this.boundZSetOps.score(o);
            span.stop(true);
            this.log(ou, span, "boundZSet_score", ctx, null, o);
            return ou;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_score", ctx, t, o);

            throw t;
        }
    }

    @Override
    public List<Double> score(Object... o) {
        return null;
    }

    public Long removeRange(long start, long end) {
        Span span = Trace.startAsync("REDIS", "boundZSet_removeRange");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundZSetOps.removeRange(start, end);
            span.stop(true);
            this.log(o, span, "boundZSet_removeRange", ctx, null, start, end);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_removeRange", ctx, t, start, end);

            throw t;
        }
    }

    @Override
    public Long removeRangeByLex(RedisZSetCommands.Range range) {
        return null;
    }

    public Long removeRangeByScore(double min, double max) {
        Span span = Trace.startAsync("REDIS", "boundZSet_removeRangeByScore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundZSetOps.removeRangeByScore(min, max);
            span.stop(true);
            this.log(o, span, "boundZSet_removeRangeByScore", ctx, null, min, max);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_removeRangeByScore", ctx, t, min, max);

            throw t;
        }
    }

    public Long unionAndStore(K otherKey, K destKey) {
        Span span = Trace.startAsync("REDIS", "boundZSet_unionAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundZSetOps.unionAndStore(otherKey, destKey);
            span.stop(true);
            this.log(o, span, "boundZSet_unionAndStore", ctx, null, otherKey, destKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_unionAndStore", ctx, t, otherKey, destKey);

            throw t;
        }
    }

    public Long unionAndStore(Collection<K> otherKeys, K destKey) {
        Span span = Trace.startAsync("REDIS", "boundZSet_unionAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundZSetOps.unionAndStore(otherKeys, destKey);
            span.stop(true);
            this.log(o, span, "boundZSet_unionAndStore", ctx, null, otherKeys, destKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_unionAndStore", ctx, t, otherKeys, destKey);

            throw t;
        }
    }

    public Long unionAndStore(Collection<K> collection, K k, RedisZSetCommands.Aggregate aggregate) {
        Span span = Trace.startAsync("REDIS", "boundZSet_unionAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundZSetOps.unionAndStore(collection, k, aggregate);
            span.stop(true);
            this.log(o, span, "boundZSet_unionAndStore", ctx, null, collection, k, aggregate);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_unionAndStore", ctx, t, collection, k, aggregate);

            throw t;
        }
    }

    public Long unionAndStore(
            Collection<K> collection, K k, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
        Span span = Trace.startAsync("REDIS", "boundZSet_unionAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundZSetOps.unionAndStore(collection, k, aggregate, weights);
            span.stop(true);
            this.log(o, span, "boundZSet_unionAndStore", ctx, null, collection, k, aggregate, weights);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_unionAndStore", ctx, t, collection, k, aggregate, weights);

            throw t;
        }
    }

    @Override
    public Set<V> difference(Collection<K> otherKeys) {
        return null;
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> differenceWithScores(Collection<K> otherKeys) {
        return null;
    }

    @Override
    public Long differenceAndStore(Collection<K> otherKeys, K destKey) {
        return null;
    }

    @Override
    public Set<V> intersect(Collection<K> otherKeys) {
        return null;
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> intersectWithScores(Collection<K> otherKeys) {
        return null;
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> intersectWithScores(
            Collection<K> otherKeys, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
        return null;
    }

    public Long intersectAndStore(K otherKey, K destKey) {
        Span span = Trace.startAsync("REDIS", "boundZSet_intersectAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundZSetOps.intersectAndStore(otherKey, destKey);
            span.stop(true);
            this.log(o, span, "boundZSet_intersectAndStore", ctx, null, otherKey, destKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_intersectAndStore", ctx, t, otherKey, destKey);

            throw t;
        }
    }

    public Long intersectAndStore(Collection<K> otherKeys, K destKey) {
        Span span = Trace.startAsync("REDIS", "boundZSet_intersectAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundZSetOps.intersectAndStore(otherKeys, destKey);
            span.stop(true);
            this.log(o, span, "boundZSet_intersectAndStore", ctx, null, otherKeys, destKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_intersectAndStore", ctx, t, otherKeys, destKey);

            throw t;
        }
    }

    public Long intersectAndStore(Collection<K> collection, K k, RedisZSetCommands.Aggregate aggregate) {
        Span span = Trace.startAsync("REDIS", "boundZSet_intersectAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundZSetOps.intersectAndStore(collection, k, aggregate);
            span.stop(true);
            this.log(o, span, "boundZSet_intersectAndStore", ctx, null, collection, k, aggregate);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_intersectAndStore", ctx, t, collection, k, aggregate);

            throw t;
        }
    }

    public Long intersectAndStore(
            Collection<K> collection, K k, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
        Span span = Trace.startAsync("REDIS", "boundZSet_intersectAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundZSetOps.intersectAndStore(collection, k, aggregate, weights);
            span.stop(true);
            this.log(o, span, "boundZSet_intersectAndStore", ctx, null, collection, k, aggregate, weights);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_intersectAndStore", ctx, t, collection, k, aggregate, weights);

            throw t;
        }
    }

    @Override
    public Set<V> union(Collection<K> otherKeys) {
        return null;
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> unionWithScores(Collection<K> otherKeys) {
        return null;
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> unionWithScores(
            Collection<K> otherKeys, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
        return null;
    }

    public Cursor<ZSetOperations.TypedTuple<V>> scan(ScanOptions options) {
        Span span = Trace.startAsync("REDIS", "boundZSet_scan");
        TraceContext ctx = Trace.currentContext();

        try {
            Cursor<ZSetOperations.TypedTuple<V>> o = this.boundZSetOps.scan(options);
            span.stop(true);
            this.log(o, span, "boundZSet_scan", ctx, null, options);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_scan", ctx, t, options);

            throw t;
        }
    }

    public Set<V> rangeByLex(RedisZSetCommands.Range range) {
        Span span = Trace.startAsync("REDIS", "boundZSet_rangeByLex");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.boundZSetOps.rangeByLex(range);
            span.stop(true);
            this.log(o, span, "boundZSet_rangeByLex", ctx, null, range);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_rangeByLex", ctx, t, range);

            throw t;
        }
    }

    public Set<V> rangeByLex(RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Span span = Trace.startAsync("REDIS", "boundZSet_rangeByLex");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.boundZSetOps.rangeByLex(range, limit);
            span.stop(true);
            this.log(o, span, "boundZSet_rangeByLex", ctx, null, range, limit);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_rangeByLex", ctx, t, range, limit);

            throw t;
        }
    }

    @Override
    public Set<V> reverseRangeByLex(RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        return null;
    }

    public RedisOperations<K, V> getOperations() {
        return this.boundZSetOps.getOperations();
    }

    public K getKey() {
        return this.boundZSetOps.getKey();
    }

    public DataType getType() {
        return this.boundZSetOps.getType();
    }

    public Long getExpire() {
        Span span = Trace.startAsync("REDIS", "boundZSet_getExpire");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.boundZSetOps.getExpire();
            span.stop(true);
            this.log(o, span, "boundZSet_getExpire", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_getExpire", ctx, t);

            throw t;
        }
    }

    public Boolean expire(long timeout, TimeUnit unit) {
        Span span = Trace.startAsync("REDIS", "boundZSet_expire");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundZSetOps.expire(timeout, unit);
            span.stop(true);
            this.log(o, span, "boundZSet_expire", ctx, null, timeout, unit);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_expire", ctx, t, timeout, unit);

            throw t;
        }
    }

    public Boolean expireAt(Date date) {
        Span span = Trace.startAsync("REDIS", "boundZSet_expireAt");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundZSetOps.expireAt(date);
            span.stop(true);
            this.log(o, span, "boundZSet_expireAt", ctx, null, date);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_expireAt", ctx, t, date);

            throw t;
        }
    }

    public Boolean persist() {
        Span span = Trace.startAsync("REDIS", "boundZSet_persist");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.boundZSetOps.persist();
            span.stop(true);
            this.log(o, span, "boundZSet_persist", ctx, null);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_persist", ctx, t);

            throw t;
        }
    }

    public void rename(K newKey) {

        Span span = Trace.startAsync("REDIS", "boundZSet_rename");
        TraceContext ctx = Trace.currentContext();

        try {
            this.boundZSetOps.rename(newKey);
            span.stop(true);
            this.log(null, span, "boundZSet_rename", ctx, null, newKey);
        } catch (Throwable t) {
            span.stop(false);
            this.log(null, span, "boundZSet_rename", ctx, t, newKey);

            throw t;
        }
    }

    void log(Object res, Span span, String serviceName, TraceContext ctx, Throwable throwable, Object... param) {
        RedisLogFormat.log(res, span, serviceName, ctx, throwable, this.toArray(this.boundZSetOps.getKey(), param));
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
