package fun.pxyc.onelogger.redis.proxy;

import fun.pxyc.onelogger.redis.RedisLogFormat;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;

public class ZSetOperationsProxy<K, V> implements ZSetOperations<K, V> {
    private final ZSetOperations<K, V> zSetOps;

    public ZSetOperationsProxy(ZSetOperations<K, V> zSetOps) {
        this.zSetOps = zSetOps;
    }

    public Boolean add(K key, V value, double score) {

        Span span = Trace.startAsync("REDIS", "zset_add");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.zSetOps.add(key, value, score);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_add", ctx, null, key, value, score);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_add", ctx, t, key, value, score);

            throw t;
        }
    }

    @Override
    public Boolean addIfAbsent(K key, V value, double score) {
        Span span = Trace.startAsync("REDIS", "zset_addIfAbsent");
        TraceContext ctx = Trace.currentContext();

        try {
            Boolean o = this.zSetOps.addIfAbsent(key, value, score);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_addIfAbsent", ctx, null, key, value, score);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_addIfAbsent", ctx, t, key, value, score);

            throw t;
        }
    }

    public Long add(K key, Set<ZSetOperations.TypedTuple<V>> typedTuples) {

        Span span = Trace.startAsync("REDIS", "zset_add");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.zSetOps.add(key, typedTuples);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_add", ctx, null, key, typedTuples);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_add", ctx, t, key, typedTuples);

            throw t;
        }
    }

    @Override
    public Long addIfAbsent(K key, Set<TypedTuple<V>> typedTuples) {
        return null;
    }

    public Long remove(K key, Object... values) {

        Span span = Trace.startAsync("REDIS", "zset_remove");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.zSetOps.remove(key, values);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_remove", ctx, null, key, values);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_remove", ctx, t, key, values);

            throw t;
        }
    }

    public Double incrementScore(K key, V value, double delta) {

        Span span = Trace.startAsync("REDIS", "zset_incrementScore");
        TraceContext ctx = Trace.currentContext();

        try {
            Double o = this.zSetOps.incrementScore(key, value, delta);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_incrementScore", ctx, null, key, value, delta);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_incrementScore", ctx, t, key, value, delta);

            throw t;
        }
    }

    @Override
    public V randomMember(K key) {
        Span span = Trace.startAsync("REDIS", "zset_randomMember");
        TraceContext ctx = Trace.currentContext();

        try {
            V o = this.zSetOps.randomMember(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_randomMember", ctx, null, key);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_randomMember", ctx, t, key);
            throw t;
        }
    }

    @Override
    public Set<V> distinctRandomMembers(K key, long count) {
        Span span = Trace.startAsync("REDIS", "zset_distinctRandomMembers");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.zSetOps.distinctRandomMembers(key, count);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_distinctRandomMembers", ctx, null, key, count);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_distinctRandomMembers", ctx, t, key, count);
            throw t;
        }
    }

    @Override
    public List<V> randomMembers(K key, long count) {
        Span span = Trace.startAsync("REDIS", "randomMembers");
        TraceContext ctx = Trace.currentContext();

        try {
            List<V> o = this.zSetOps.randomMembers(key, count);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_randomMembers", ctx, null, key, count);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_randomMembers", ctx, t, key, count);
            throw t;
        }
    }

    @Override
    public TypedTuple<V> randomMemberWithScore(K key) {
        Span span = Trace.startAsync("REDIS", "zset_randomMemberWithScore");
        TraceContext ctx = Trace.currentContext();

        try {
            TypedTuple<V> o = this.zSetOps.randomMemberWithScore(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_randomMemberWithScore", ctx, null, key);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_randomMemberWithScore", ctx, t, key);
            throw t;
        }
    }

    @Override
    public Set<TypedTuple<V>> distinctRandomMembersWithScore(K key, long count) {
        Span span = Trace.startAsync("REDIS", "zset_distinctRandomMembersWithScore");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<TypedTuple<V>> o = this.zSetOps.distinctRandomMembersWithScore(key, count);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_distinctRandomMembersWithScore", ctx, null, key, count);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_distinctRandomMembersWithScore", ctx, t, key, count);
            throw t;
        }
    }

    @Override
    public List<TypedTuple<V>> randomMembersWithScore(K key, long count) {
        Span span = Trace.startAsync("REDIS", "zset_randomMembersWithScore");
        TraceContext ctx = Trace.currentContext();

        try {
            List<TypedTuple<V>> o = this.zSetOps.randomMembersWithScore(key, count);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_randomMembersWithScore", ctx, null, key, count);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_randomMembersWithScore", ctx, t, key, count);
            throw t;
        }
    }

    public Long rank(K key, Object o) {

        Span span = Trace.startAsync("REDIS", "zset_rank");
        TraceContext ctx = Trace.currentContext();

        try {
            Long lo = this.zSetOps.rank(key, o);
            span.stop(true);
            RedisLogFormat.log(lo, span, "zset_rank", ctx, null, key, o);
            return lo;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_rank", ctx, t, key, o);

            throw t;
        }
    }

    public Long reverseRank(K key, Object o) {

        Span span = Trace.startAsync("REDIS", "zset_reverseRank");
        TraceContext ctx = Trace.currentContext();

        try {
            Long lo = this.zSetOps.reverseRank(key, o);
            span.stop(true);
            RedisLogFormat.log(lo, span, "zset_reverseRank", ctx, null, key, o);
            return lo;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_reverseRank", ctx, t, key, o);

            throw t;
        }
    }

    public Set<V> range(K key, long start, long end) {

        Span span = Trace.startAsync("REDIS", "zset_range");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.zSetOps.range(key, start, end);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_range", ctx, null, key, start, end);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_range", ctx, t, key, start, end);

            throw t;
        }
    }

    public Set<ZSetOperations.TypedTuple<V>> rangeWithScores(K key, long start, long end) {

        Span span = Trace.startAsync("REDIS", "zset_rangeWithScores");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<ZSetOperations.TypedTuple<V>> o = this.zSetOps.rangeWithScores(key, start, end);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_rangeWithScores", ctx, null, key, start, end);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_rangeWithScores", ctx, t, key, key, start, end);

            throw t;
        }
    }

    public Set<V> rangeByScore(K key, double min, double max) {

        Span span = Trace.startAsync("REDIS", "zset_rangeByScore");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.zSetOps.rangeByScore(key, min, max);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_rangeByScore", ctx, null, key, min, max);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_rangeByScore", ctx, t, key, min, max);

            throw t;
        }
    }

    public Set<ZSetOperations.TypedTuple<V>> rangeByScoreWithScores(K key, double min, double max) {

        Span span = Trace.startAsync("REDIS", "zset_rangeByScoreWithScores");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<ZSetOperations.TypedTuple<V>> o = this.zSetOps.rangeByScoreWithScores(key, min, max);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_rangeByScoreWithScores", ctx, null, key, min, max);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_rangeByScoreWithScores", ctx, t, key, min, max);

            throw t;
        }
    }

    public Set<V> rangeByScore(K key, double min, double max, long offset, long count) {

        Span span = Trace.startAsync("REDIS", "zset_rangeByScore");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.zSetOps.rangeByScore(key, min, max, offset, count);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_rangeByScore", ctx, null, key, min, max, offset, count);
            return o;
        } catch (Throwable t3) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_rangeByScore", ctx, t3, key, min, max, offset, count);

            throw t3;
        }
    }

    public Set<ZSetOperations.TypedTuple<V>> rangeByScoreWithScores(
            K key, double min, double max, long offset, long count) {

        Span span = Trace.startAsync("REDIS", "zset_rangeByScoreWithScores");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<ZSetOperations.TypedTuple<V>> o = this.zSetOps.rangeByScoreWithScores(key, min, max, offset, count);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_rangeByScoreWithScores", ctx, null, key, min, max, offset, count);
            return o;
        } catch (Throwable t3) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_rangeByScoreWithScores", ctx, t3, key, min, max, offset, count);

            throw t3;
        }
    }

    public Set<V> reverseRange(K key, long start, long end) {

        Span span = Trace.startAsync("REDIS", "zset_reverseRange");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.zSetOps.reverseRange(key, start, end);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_reverseRange", ctx, null, key, start, end);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_reverseRange", ctx, t, key, start, end);

            throw t;
        }
    }

    public Set<ZSetOperations.TypedTuple<V>> reverseRangeWithScores(K key, long start, long end) {

        Span span = Trace.startAsync("REDIS", "zset_reverseRangeWithScores");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<ZSetOperations.TypedTuple<V>> o = this.zSetOps.reverseRangeWithScores(key, start, end);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_reverseRangeWithScores", ctx, null, key, start, end);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_reverseRangeWithScores", ctx, t, key, start, end);

            throw t;
        }
    }

    public Set<V> reverseRangeByScore(K key, double min, double max) {

        Span span = Trace.startAsync("REDIS", "zset_reverseRangeByScore");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.zSetOps.reverseRangeByScore(key, min, max);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_reverseRangeByScore", ctx, null, key, min, max);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_reverseRangeByScore", ctx, t, key, min, max);

            throw t;
        }
    }

    public Set<ZSetOperations.TypedTuple<V>> reverseRangeByScoreWithScores(K key, double min, double max) {

        Span span = Trace.startAsync("REDIS", "zset_reverseRangeByScoreWithScores");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<ZSetOperations.TypedTuple<V>> o = this.zSetOps.reverseRangeByScoreWithScores(key, min, max);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_reverseRangeByScoreWithScores", ctx, null, key, min, max);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_reverseRangeByScoreWithScores", ctx, t, key, min, max);

            throw t;
        }
    }

    public Set<V> reverseRangeByScore(K key, double min, double max, long offset, long count) {

        Span span = Trace.startAsync("REDIS", "zset_reverseRangeByScore");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.zSetOps.reverseRangeByScore(key, min, max, offset, count);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_reverseRangeByScore", ctx, null, key, min, max, offset, count);
            return o;
        } catch (Throwable t3) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_reverseRangeByScore", ctx, t3, key, min, max, offset, count);

            throw t3;
        }
    }

    public Set<ZSetOperations.TypedTuple<V>> reverseRangeByScoreWithScores(
            K key, double min, double max, long offset, long count) {

        Span span = Trace.startAsync("REDIS", "zset_reverseRangeByScoreWithScores");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<ZSetOperations.TypedTuple<V>> o =
                    this.zSetOps.reverseRangeByScoreWithScores(key, min, max, offset, count);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_reverseRangeByScoreWithScores", ctx, null, key, min, max, offset, count);
            return o;
        } catch (Throwable t3) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_reverseRangeByScoreWithScores", ctx, t3, key, min, max, offset, count);

            throw t3;
        }
    }

    public Long count(K key, double min, double max) {

        Span span = Trace.startAsync("REDIS", "zset_count");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.zSetOps.count(key, min, max);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_count", ctx, null, key, min, max);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_count", ctx, t, key, min, max);

            throw t;
        }
    }

    @Override
    public Long lexCount(K k, RedisZSetCommands.Range range) {
        Span span = Trace.startAsync("REDIS", "zset_lexCount");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.zSetOps.lexCount(k, range);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_lexCount", ctx, null, k, range);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_lexCount", ctx, t, k, range);

            throw t;
        }
    }

    @Override
    public ZSetOperations.TypedTuple<V> popMin(K k) {
        Span span = Trace.startAsync("REDIS", "zset_popMin");
        TraceContext ctx = Trace.currentContext();

        try {
            ZSetOperations.TypedTuple<V> o = this.zSetOps.popMin(k);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_popMin", ctx, null, k);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_popMin", ctx, t, k);
            throw t;
        }
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> popMin(K k, long count) {
        Span span = Trace.startAsync("REDIS", "zset_popMin");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<ZSetOperations.TypedTuple<V>> o = this.zSetOps.popMin(k, count);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_popMin", ctx, null, k, count);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_popMin", ctx, t, k, count);
            throw t;
        }
    }

    @Override
    public ZSetOperations.TypedTuple<V> popMin(K k, long timeout, TimeUnit unit) {
        Span span = Trace.startAsync("REDIS", "zset_popMin");
        TraceContext ctx = Trace.currentContext();

        try {
            ZSetOperations.TypedTuple<V> o = this.zSetOps.popMin(k, timeout, unit);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_popMin", ctx, null, k, timeout, unit);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_popMin", ctx, t, k, timeout, unit);
            throw t;
        }
    }

    @Override
    public ZSetOperations.TypedTuple<V> popMax(K k) {
        Span span = Trace.startAsync("REDIS", "zset_popMax");
        TraceContext ctx = Trace.currentContext();

        try {
            ZSetOperations.TypedTuple<V> o = this.zSetOps.popMax(k);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_popMax", ctx, null, k);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_popMax", ctx, t, k);
            throw t;
        }
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> popMax(K k, long count) {
        Span span = Trace.startAsync("REDIS", "zset_popMax");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<ZSetOperations.TypedTuple<V>> o = this.zSetOps.popMax(k, count);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_popMax", ctx, null, k, count);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_popMax", ctx, t, k, count);
            throw t;
        }
    }

    @Override
    public ZSetOperations.TypedTuple<V> popMax(K k, long timeout, TimeUnit unit) {
        Span span = Trace.startAsync("REDIS", "zset_popMax");
        TraceContext ctx = Trace.currentContext();

        try {
            ZSetOperations.TypedTuple<V> o = this.zSetOps.popMax(k, timeout, unit);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_popMax", ctx, null, k, timeout, unit);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_popMax", ctx, t, k, timeout, unit);
            throw t;
        }
    }

    public Long size(K key) {

        Span span = Trace.startAsync("REDIS", "zset_size");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.zSetOps.size(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_size", ctx, null, key);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_size", ctx, t, key);

            throw t;
        }
    }

    public Long zCard(K key) {

        Span span = Trace.startAsync("REDIS", "zset_zCard");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.zSetOps.zCard(key);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_zCard", ctx, null, key);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_zCard", ctx, t, key);

            throw t;
        }
    }

    public Double score(K key, Object o) {

        Span span = Trace.startAsync("REDIS", "zset_score");
        TraceContext ctx = Trace.currentContext();

        try {
            Double dou = this.zSetOps.score(key, o);
            span.stop(true);
            RedisLogFormat.log(dou, span, "zset_score", ctx, null, key, o);
            return dou;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_score", ctx, t, key, o);

            throw t;
        }
    }

    @Override
    public List<Double> score(K key, Object... o) {
        return null;
    }

    public Long removeRange(K key, long start, long end) {

        Span span = Trace.startAsync("REDIS", "zset_removeRange");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.zSetOps.removeRange(key, start, end);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_removeRange", ctx, null, key, start, end);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_removeRange", ctx, t, key, start, end);

            throw t;
        }
    }

    @Override
    public Long removeRangeByLex(K key, RedisZSetCommands.Range range) {
        return null;
    }

    public Long removeRangeByScore(K key, double min, double max) {

        Span span = Trace.startAsync("REDIS", "zset_removeRangeByScore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.zSetOps.removeRangeByScore(key, min, max);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_removeRangeByScore", ctx, null, key, min, max);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_removeRangeByScore", ctx, t, key, min, max);

            throw t;
        }
    }

    @Override
    public Set<V> difference(K key, Collection<K> otherKeys) {
        return null;
    }

    @Override
    public Set<TypedTuple<V>> differenceWithScores(K key, Collection<K> otherKeys) {
        return null;
    }

    @Override
    public Long differenceAndStore(K key, Collection<K> otherKeys, K destKey) {
        return null;
    }

    @Override
    public Set<V> intersect(K key, Collection<K> otherKeys) {
        return null;
    }

    @Override
    public Set<TypedTuple<V>> intersectWithScores(K key, Collection<K> otherKeys) {
        return null;
    }

    @Override
    public Set<TypedTuple<V>> intersectWithScores(
            K key, Collection<K> otherKeys, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
        return null;
    }

    public Long unionAndStore(K key, K otherKey, K destKey) {

        Span span = Trace.startAsync("REDIS", "zset_unionAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.zSetOps.unionAndStore(key, otherKey, destKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_unionAndStore", ctx, null, key, otherKey, destKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_unionAndStore", ctx, t, key, otherKey, destKey);

            throw t;
        }
    }

    public Long unionAndStore(K key, Collection<K> otherKeys, K destKey) {

        Span span = Trace.startAsync("REDIS", "zset_unionAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.zSetOps.unionAndStore(key, otherKeys, destKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_unionAndStore", ctx, null, key, otherKeys, destKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_unionAndStore", ctx, t, key, otherKeys, destKey);

            throw t;
        }
    }

    public Long unionAndStore(
            K key,
            Collection<K> otherKeys,
            K destKey,
            RedisZSetCommands.Aggregate aggregate,
            RedisZSetCommands.Weights weights) {

        Span span = Trace.startAsync("REDIS", "zset_unionAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.zSetOps.unionAndStore(key, otherKeys, destKey, aggregate, weights);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_unionAndStore", ctx, null, key, otherKeys, destKey, aggregate, weights);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_unionAndStore", ctx, t, key, otherKeys, destKey, aggregate, weights);

            throw t;
        }
    }

    public Long intersectAndStore(K key, K otherKey, K destKey) {

        Span span = Trace.startAsync("REDIS", "zset_intersectAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.zSetOps.intersectAndStore(key, otherKey, destKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_intersectAndStore", ctx, null, key, otherKey, destKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_intersectAndStore", ctx, t, key, otherKey, destKey);

            throw t;
        }
    }

    public Long intersectAndStore(K key, Collection<K> otherKeys, K destKey) {

        Span span = Trace.startAsync("REDIS", "zset_intersectAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.zSetOps.intersectAndStore(key, otherKeys, destKey);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_intersectAndStore", ctx, null, key, otherKeys, destKey);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_intersectAndStore", ctx, t, key, otherKeys, destKey);

            throw t;
        }
    }

    public Long intersectAndStore(
            K key,
            Collection<K> otherKeys,
            K destKey,
            RedisZSetCommands.Aggregate aggregate,
            RedisZSetCommands.Weights weights) {

        Span span = Trace.startAsync("REDIS", "zset_intersectAndStore");
        TraceContext ctx = Trace.currentContext();

        try {
            Long o = this.zSetOps.intersectAndStore(key, otherKeys, destKey, aggregate, weights);
            span.stop(true);
            RedisLogFormat.log(
                    o, span, "zset_intersectAndStore", ctx, null, key, otherKeys, destKey, aggregate, weights);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(
                    null, span, "zset_intersectAndStore", ctx, t, key, otherKeys, destKey, aggregate, weights);

            throw t;
        }
    }

    @Override
    public Set<V> union(K key, Collection<K> otherKeys) {
        return null;
    }

    @Override
    public Set<TypedTuple<V>> unionWithScores(K key, Collection<K> otherKeys) {
        return null;
    }

    @Override
    public Set<TypedTuple<V>> unionWithScores(
            K key, Collection<K> otherKeys, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
        return null;
    }

    public Cursor<ZSetOperations.TypedTuple<V>> scan(K key, ScanOptions options) {

        Span span = Trace.startAsync("REDIS", "zset_scan");
        TraceContext ctx = Trace.currentContext();

        try {
            Cursor<ZSetOperations.TypedTuple<V>> o = this.zSetOps.scan(key, options);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_scan", ctx, null, key, options);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_scan", ctx, t, key, options);

            throw t;
        }
    }

    public Set<V> rangeByLex(K key, RedisZSetCommands.Range range) {

        Span span = Trace.startAsync("REDIS", "zset_rangeByLex");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.zSetOps.rangeByLex(key, range);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_rangeByLex", ctx, null, key, range);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_rangeByLex", ctx, t, key, range);

            throw t;
        }
    }

    public Set<V> rangeByLex(K key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {

        Span span = Trace.startAsync("REDIS", "zset_rangeByLex");
        TraceContext ctx = Trace.currentContext();

        try {
            Set<V> o = this.zSetOps.rangeByLex(key, range, limit);
            span.stop(true);
            RedisLogFormat.log(o, span, "zset_rangeByLex", ctx, null, key, range, limit);
            return o;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "zset_rangeByLex", ctx, t, key, range, limit);

            throw t;
        }
    }

    @Override
    public Set<V> reverseRangeByLex(K key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        return null;
    }

    public RedisOperations<K, V> getOperations() {
        return this.zSetOps.getOperations();
    }
}
