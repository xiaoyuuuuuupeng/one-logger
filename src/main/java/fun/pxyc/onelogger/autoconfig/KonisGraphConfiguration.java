package fun.pxyc.onelogger.autoconfig;

import fun.pxyc.onelogger.konisgraph.KonisGraphAuditLogAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "graph.config", name = "address")
@ConfigurationProperties(prefix = "graph.config")
public class KonisGraphConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "konisGraphAuditLogAspect")
    public KonisGraphAuditLogAspect konisGraphAuditLogAspect() {
        return new KonisGraphAuditLogAspect();
    }
}
