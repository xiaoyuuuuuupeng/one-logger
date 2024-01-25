package fun.pxyc.onelogger.autoconfig;

import fun.pxyc.onelogger.bizlog.BizAdvice;
import fun.pxyc.onelogger.web.WebDynamicPointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BizAopConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BizAopConfiguration.class);

    @Bean
    public Pointcut bizPointCut(EnvConfigProps envConfigProps) {
        WebDynamicPointcut webDynamicPointcut = new WebDynamicPointcut();
        webDynamicPointcut.setExpression(envConfigProps.getBizCutPoint());
        return webDynamicPointcut;
    }

    @Bean
    public BizAdvice getBizAdvice() {
        return new BizAdvice();
    }

    @Bean
    public DefaultPointcutAdvisor bizPointcutAdvisor(EnvConfigProps envConfigProps) {
        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
        defaultPointcutAdvisor.setPointcut(bizPointCut(envConfigProps));
        defaultPointcutAdvisor.setAdvice(getBizAdvice());
        log.info("biz log aop init...");
        return defaultPointcutAdvisor;
    }
}
