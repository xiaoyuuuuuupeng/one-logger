package fun.pxyc.onelogger.redis.proxy;

import fun.pxyc.onelogger.redis.RedisLogFormat;
import fun.pxyc.onelogger.trace.Span;
import fun.pxyc.onelogger.trace.Trace;
import fun.pxyc.onelogger.trace.TraceContext;
import java.util.List;
import java.util.Map;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.data.redis.domain.geo.GeoShape;

public class GeoOperationsProxy<K, V> implements GeoOperations<K, V> {
    private final GeoOperations<K, V> geoOps;

    public GeoOperationsProxy(GeoOperations<K, V> geoOps) {
        this.geoOps = geoOps;
    }

    public Long add(K key, Point point, V member) {

        Span span = Trace.startAsync("REDIS", "geo_add");
        TraceContext ctx = Trace.currentContext();

        try {
            Long ret = this.geoOps.add(key, point, member);
            span.stop(true);
            RedisLogFormat.log(ret, span, "geo_add", ctx, null, key, point, member);
            return ret;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "geo_add", ctx, t, key, point, member);

            throw t;
        }
    }

    public Long add(K key, RedisGeoCommands.GeoLocation<V> location) {

        Span span = Trace.startAsync("REDIS", "geo_add");
        TraceContext ctx = Trace.currentContext();

        try {
            Long ret = this.geoOps.add(key, location);
            span.stop(true);
            RedisLogFormat.log(ret, span, "geo_add", ctx, null, key, location);
            return ret;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "geo_add", ctx, t, key, location);

            throw t;
        }
    }

    public Long add(K key, Map<V, Point> memberCoordinateMap) {

        Span span = Trace.startAsync("REDIS", "geo_add");
        TraceContext ctx = Trace.currentContext();

        try {
            Long ret = this.geoOps.add(key, memberCoordinateMap);
            span.stop(true);
            RedisLogFormat.log(ret, span, "geo_add", ctx, null, key, memberCoordinateMap);
            return ret;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "geo_add", ctx, t, key, memberCoordinateMap);

            throw t;
        }
    }

    public Long add(K key, Iterable<RedisGeoCommands.GeoLocation<V>> geoLocations) {

        Span span = Trace.startAsync("REDIS", "geo_add");
        TraceContext ctx = Trace.currentContext();

        try {
            Long ret = this.geoOps.add(key, geoLocations);
            span.stop(true);
            RedisLogFormat.log(ret, span, "geo_add", ctx, null, key, geoLocations);
            return ret;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "geo_add", ctx, t, key, geoLocations);

            throw t;
        }
    }

    public Distance distance(K key, V member1, V member2) {

        Span span = Trace.startAsync("REDIS", "geo_distance");
        TraceContext ctx = Trace.currentContext();

        try {
            Distance ret = this.geoOps.distance(key, member1, member2);
            span.stop(true);
            RedisLogFormat.log(ret, span, "geo_distance", ctx, null, key, member1, member2);
            return ret;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "geo_distance", ctx, t, key, member1, member2);

            throw t;
        }
    }

    public Distance distance(K key, V member1, V member2, Metric metric) {

        Span span = Trace.startAsync("REDIS", "geo_distance");
        TraceContext ctx = Trace.currentContext();

        try {
            Distance ret = this.geoOps.distance(key, member1, member2, metric);
            span.stop(true);
            RedisLogFormat.log(ret, span, "geo_distance", ctx, null, key, member1, member2, metric);
            return ret;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "geo_distance", ctx, t, key, member1, member2, metric);

            throw t;
        }
    }

    public List<String> hash(K key, V... members) {

        Span span = Trace.startAsync("REDIS", "geo_hash");
        TraceContext ctx = Trace.currentContext();

        try {
            List<String> ret = this.geoOps.hash(key, members);
            span.stop(true);
            RedisLogFormat.log(ret, span, "geo_hash", ctx, null, key, members);
            return ret;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "geo_hash", ctx, t, key, members);

            throw t;
        }
    }

    public List<Point> position(K key, V... members) {

        Span span = Trace.startAsync("REDIS", "geo_position");
        TraceContext ctx = Trace.currentContext();

        try {
            List<Point> ret = this.geoOps.position(key, members);
            span.stop(true);
            RedisLogFormat.log(ret, span, "geo_position", ctx, null, key, members);
            return ret;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "geo_position", ctx, t, key, members);

            throw t;
        }
    }

    public GeoResults<RedisGeoCommands.GeoLocation<V>> radius(K key, Circle within) {

        Span span = Trace.startAsync("REDIS", "geo_radius");
        TraceContext ctx = Trace.currentContext();

        try {
            GeoResults<RedisGeoCommands.GeoLocation<V>> ret = this.geoOps.radius(key, within);
            span.stop(true);
            RedisLogFormat.log(ret, span, "geo_radius", ctx, null, key, within);
            return ret;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "geo_radius", ctx, t, key, within);

            throw t;
        }
    }

    public GeoResults<RedisGeoCommands.GeoLocation<V>> radius(
            K key, Circle within, RedisGeoCommands.GeoRadiusCommandArgs args) {

        Span span = Trace.startAsync("REDIS", "geo_radius");
        TraceContext ctx = Trace.currentContext();

        try {
            GeoResults<RedisGeoCommands.GeoLocation<V>> ret = this.geoOps.radius(key, within, args);
            span.stop(true);
            RedisLogFormat.log(ret, span, "geo_radius", ctx, null, key, within, args);
            return ret;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "geo_radius", ctx, t, key, within, args);

            throw t;
        }
    }

    public GeoResults<RedisGeoCommands.GeoLocation<V>> radius(K key, V member, double radius) {

        Span span = Trace.startAsync("REDIS", "geo_radius");
        TraceContext ctx = Trace.currentContext();

        try {
            GeoResults<RedisGeoCommands.GeoLocation<V>> ret = this.geoOps.radius(key, member, radius);
            span.stop(true);
            RedisLogFormat.log(ret, span, "geo_radius", ctx, null, key, member, radius);
            return ret;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "geo_radius", ctx, t, key, member, radius);

            throw t;
        }
    }

    public GeoResults<RedisGeoCommands.GeoLocation<V>> radius(K key, V member, Distance distance) {

        Span span = Trace.startAsync("REDIS", "geo_radius");
        TraceContext ctx = Trace.currentContext();

        try {
            GeoResults<RedisGeoCommands.GeoLocation<V>> ret = this.geoOps.radius(key, member, distance);
            span.stop(true);
            RedisLogFormat.log(ret, span, "geo_radius", ctx, null, key, member, distance);
            return ret;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "geo_radius", ctx, t, key, member, distance);

            throw t;
        }
    }

    public GeoResults<RedisGeoCommands.GeoLocation<V>> radius(
            K key, V member, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args) {

        Span span = Trace.startAsync("REDIS", "geo_radius");
        TraceContext ctx = Trace.currentContext();

        try {
            GeoResults<RedisGeoCommands.GeoLocation<V>> ret = this.geoOps.radius(key, member, distance, args);
            span.stop(true);
            RedisLogFormat.log(ret, span, "geo_radius", ctx, null, key, member, distance, args);
            return ret;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "geo_radius", ctx, t, key, member, distance, args);

            throw t;
        }
    }

    public Long remove(K key, V... members) {

        Span span = Trace.startAsync("REDIS", "geo_remove");
        TraceContext ctx = Trace.currentContext();

        try {
            Long ret = this.geoOps.remove(key, members);
            span.stop(true);
            RedisLogFormat.log(ret, span, "geo_remove", ctx, null, key, members);
            return ret;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "geo_remove", ctx, t, key, members);

            throw t;
        }
    }

    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<V>> search(
            K key, GeoReference<V> reference, GeoShape geoPredicate, RedisGeoCommands.GeoSearchCommandArgs args) {
        Span span = Trace.startAsync("REDIS", "geo_search");
        TraceContext ctx = Trace.currentContext();

        try {
            GeoResults<RedisGeoCommands.GeoLocation<V>> ret = this.geoOps.search(key, reference, geoPredicate, args);
            span.stop(true);
            RedisLogFormat.log(ret, span, "geo_search", ctx, null, key, reference, geoPredicate, args);
            return ret;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "geo_search", ctx, t, key, reference, geoPredicate, args);

            throw t;
        }
    }

    @Override
    public Long searchAndStore(
            K key,
            K destKey,
            GeoReference<V> reference,
            GeoShape geoPredicate,
            RedisGeoCommands.GeoSearchStoreCommandArgs args) {
        Span span = Trace.startAsync("REDIS", "geo_search");
        TraceContext ctx = Trace.currentContext();

        try {
            Long ret = this.geoOps.searchAndStore(key, destKey, reference, geoPredicate, args);
            span.stop(true);
            RedisLogFormat.log(ret, span, "geo_search", ctx, null, key, destKey, reference, geoPredicate, args);
            return ret;
        } catch (Throwable t) {
            span.stop(false);
            RedisLogFormat.log(null, span, "geo_search", ctx, t, key, destKey, reference, geoPredicate, args);

            throw t;
        }
    }
}
