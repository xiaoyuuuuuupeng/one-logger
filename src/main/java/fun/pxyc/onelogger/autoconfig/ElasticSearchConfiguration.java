package fun.pxyc.onelogger.autoconfig;

import fun.pxyc.onelogger.elasticsearch.ElasticSearchAuditLogAspect;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({RestClient.class})
@ConditionalOnProperty(prefix = "elasticsearch", name = "hosts")
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticSearchConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "elasticSearchAuditLogAspect")
    public ElasticSearchAuditLogAspect elasticSearchAuditLogAspect() {
        return new ElasticSearchAuditLogAspect();
    }
}
