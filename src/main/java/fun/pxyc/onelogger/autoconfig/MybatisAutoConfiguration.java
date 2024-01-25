package fun.pxyc.onelogger.autoconfig;

import fun.pxyc.onelogger.mybatis.SqlStatsInterceptor;
import java.util.Map;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({Interceptor.class, SqlSessionFactory.class})
public class MybatisAutoConfiguration implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, SqlSessionFactory> sqlSessionFactoryMap =
                applicationContext.getBeansOfType(SqlSessionFactory.class);
        sqlSessionFactoryMap.values().forEach(v -> v.getConfiguration().addInterceptor(new SqlStatsInterceptor()));
    }
}
