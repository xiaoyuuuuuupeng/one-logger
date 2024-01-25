package fun.pxyc.onelogger.autoconfig;

import fun.pxyc.onelogger.db.DbDyeing;
import fun.pxyc.onelogger.db.JdbcAuditLogAspect;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnClass({JdbcTemplate.class})
public class JdbcAutoConfiguration {

    @Value("${envconfig.db.prefix:t_,v_}")
    String tablePrefix;

    public JdbcAutoConfiguration() {}

    @PostConstruct
    void init() {
        String[] ss = this.tablePrefix.split(",");
        DbDyeing.init(ss);
    }

    @Bean
    JdbcAuditLogAspect jdbcAuditLogAspect() {
        return new JdbcAuditLogAspect();
    }
}
