package fun.pxyc.onelogger.autoconfig;

import fun.pxyc.onelogger.web.TraceIdInterceptor;
import fun.pxyc.onelogger.web.WebDynamicPointcut;
import fun.pxyc.onelogger.web.WebLogInterceptor;
import javax.servlet.http.HttpServletRequest;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnClass({HttpServletRequest.class, HandlerInterceptor.class})
public class WebControllerConfiguration implements WebMvcConfigurer {

    @Bean
    public Pointcut customPointCut(EnvConfigProps envConfigProps) {
        WebDynamicPointcut webDynamicPointcut = new WebDynamicPointcut();
        webDynamicPointcut.setExpression(envConfigProps.getWebCutPoint());
        return webDynamicPointcut;
    }

    @Bean
    public WebLogInterceptor getAdvice() {
        return new WebLogInterceptor();
    }

    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor(EnvConfigProps envConfigProps) {
        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
        defaultPointcutAdvisor.setPointcut(customPointCut(envConfigProps));
        defaultPointcutAdvisor.setAdvice(getAdvice());
        return defaultPointcutAdvisor;
    }

    @Bean
    public TraceIdInterceptor traceIdInterceptor() {
        return new TraceIdInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(traceIdInterceptor());
    }
}
