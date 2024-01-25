package fun.pxyc.onelogger.autoconfig;

import fun.pxyc.onelogger.kafka.KafkaAuditLogListenerAspect;
import fun.pxyc.onelogger.kafka.KafkaAuditLogSendAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableKafka
@ConditionalOnClass({KafkaTemplate.class})
public class KafkaAutoConfiguration {
    public KafkaAutoConfiguration() {}

    @Bean
    KafkaAuditLogListenerAspect kafkaAuditLogListenerAspect() {
        return new KafkaAuditLogListenerAspect();
    }

    @Bean
    KafkaAuditLogSendAspect kafkaAuditSendAspect() {
        return new KafkaAuditLogSendAspect();
    }
}
