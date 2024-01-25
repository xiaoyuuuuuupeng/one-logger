package fun.pxyc.onelogger.autoconfig;

import fun.pxyc.onelogger.redis.RedisAuditLogAspect;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ConditionalOnClass({RedisTemplate.class})
public class RedisAutoConfiguration {
    @Value("${spring.redis.enablePeriodicRefresh:true}")
    boolean enablePeriodicRefresh = true;

    @Value("${spring.redis.refreshPeriod:15}")
    int refreshPeriod = 15;

    @Value("${spring.redis.validateClusterNodeMembership:true}")
    boolean validateClusterNodeMembership = true;

    @Value("${spring.redis.cluster.maxRedirects:5}")
    int maxRedirects;

    public RedisAutoConfiguration() {}

    @Bean
    RedisAuditLogAspect redisAuditLogAspect() {
        return new RedisAuditLogAspect();
    }

    @Bean
    LettuceClientConfigurationBuilderCustomizer lettuceCustomizer() {
        return new TopologyRefreshCustomizer();
    }

    class TopologyRefreshCustomizer implements LettuceClientConfigurationBuilderCustomizer {
        TopologyRefreshCustomizer() {}

        public void customize(LettuceClientConfiguration.LettuceClientConfigurationBuilder clientConfigurationBuilder) {
            ClusterTopologyRefreshOptions o1 = ClusterTopologyRefreshOptions.builder()
                    .enablePeriodicRefresh(RedisAutoConfiguration.this.enablePeriodicRefresh)
                    .refreshPeriod(Duration.ofSeconds(RedisAutoConfiguration.this.refreshPeriod))
                    .build();
            SocketOptions sockOptions = SocketOptions.builder().tcpNoDelay(true).build();
            ClusterClientOptions c = ClusterClientOptions.builder()
                    .socketOptions(sockOptions)
                    .maxRedirects(RedisAutoConfiguration.this.maxRedirects)
                    .topologyRefreshOptions(o1)
                    .validateClusterNodeMembership(RedisAutoConfiguration.this.validateClusterNodeMembership)
                    .build();
            clientConfigurationBuilder.clientOptions(c);
        }
    }
}
