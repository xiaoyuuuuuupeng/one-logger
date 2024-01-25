package fun.pxyc.onelogger.autoconfig;

import fun.pxyc.onelogger.http.HttpAuditLogInterceptor;
import fun.pxyc.onelogger.http.HttpClientProperties;
import fun.pxyc.onelogger.http.HttpsClientRequestFactory;
import fun.pxyc.onelogger.http.RestTemplateAspect;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConditionalOnClass({RestTemplate.class})
@Import({RestTemplateWithPoolConfiguration.class})
public class RestTemplateConfiguration implements ApplicationListener<ApplicationEvent> {
    public RestTemplateConfiguration() {}

    @Bean
    @ConfigurationProperties(prefix = "spring.resttemplate")
    @ConditionalOnMissingBean(name = {"httpClientProperties"})
    HttpClientProperties httpClientProperties() {
        return new HttpClientProperties();
    }

    @Bean
    @ConditionalOnMissingBean(name = {"clientHttpRequestFactory"})
    ClientHttpRequestFactory clientHttpRequestFactory(HttpClientProperties httpClientProperties) {
        if (httpClientProperties.isUsePool()) {
            throw new RuntimeException(
                    "spring.resttemplate.usePool = true depends on org.apache.httpcomponents:httpclient:4.5.2 ");
        } else {
            httpClientProperties.setMaxConnectPerRoute(0);
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(httpClientProperties.getConnectTimeout());
            factory.setReadTimeout(httpClientProperties.getReadTimeout());
            return factory;
        }
    }

    @Bean
    @ConditionalOnMissingBean(name = {"httpsClientHttpRequestFactory"})
    ClientHttpRequestFactory httpsClientHttpRequestFactory(HttpClientProperties httpClientProperties) {
        if (httpClientProperties.isUsePool()) {
            throw new RuntimeException(
                    "spring.resttemplate.usePool = true depends on org.apache.httpcomponents:httpclient:4.5.2 ");
        } else {
            httpClientProperties.setMaxConnectPerRoute(0);
            HttpsClientRequestFactory factory = new HttpsClientRequestFactory();
            factory.setConnectTimeout(httpClientProperties.getConnectTimeout());
            factory.setReadTimeout(httpClientProperties.getReadTimeout());
            return factory;
        }
    }

    @Bean
    @ConditionalOnMissingBean(name = {"restTemplate"})
    RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        return getRestTemplate(clientHttpRequestFactory);
    }

    private RestTemplate getRestTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        List<HttpMessageConverter<?>> cnvts = restTemplate.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = cnvts.iterator();

        while (iterator.hasNext()) {
            HttpMessageConverter<?> c = iterator.next();
            if (c instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) c).setDefaultCharset(StandardCharsets.UTF_8);
            }
        }

        return restTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(name = {"httpsRestTemplate"})
    RestTemplate httpsRestTemplate(ClientHttpRequestFactory httpsClientHttpRequestFactory) {
        return getRestTemplate(httpsClientHttpRequestFactory);
    }

    @Bean
    RestTemplateAspect restTemplateAspect(HttpClientProperties httpClientProperties) {
        RestTemplateAspect.logAllHeaders = httpClientProperties.isLogAllHeaders();
        RestTemplateAspect.initLogHeaderNames(httpClientProperties.getLogHeaderNames());
        return new RestTemplateAspect();
    }

    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            this.addInterceptor(((ContextRefreshedEvent) event).getApplicationContext());
        }
    }

    private void addInterceptor(ApplicationContext context) {
        Collection<RestTemplate> templates =
                context.getBeansOfType(RestTemplate.class).values();
        if (!templates.isEmpty()) {
            templates.forEach(this::setInterceptor);
        }
    }

    private void setInterceptor(RestTemplate r) {
        if (r.getInterceptors() == null) {
            r.setInterceptors(new ArrayList());
        }

        r.getInterceptors().add(new HttpAuditLogInterceptor());
    }
}
