package fun.pxyc.onelogger.autoconfig;

import fun.pxyc.onelogger.http.HttpClientProperties;
import fun.pxyc.onelogger.http.HttpsClientRequestFactory;
import javax.net.ssl.SSLContext;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@ConditionalOnClass({HttpClient.class})
public class RestTemplateWithPoolConfiguration {
    public RestTemplateWithPoolConfiguration() {}

    @Bean
    @ConditionalOnMissingBean(name = {"clientHttpRequestFactory"})
    ClientHttpRequestFactory clientHttpRequestFactory(HttpClientProperties httpClientProperties) {
        if (!httpClientProperties.isUsePool()) {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(httpClientProperties.getConnectTimeout());
            factory.setReadTimeout(httpClientProperties.getReadTimeout());
            return factory;
        } else {
            HttpClient httpClient = HttpClientBuilder.create()
                    .setMaxConnTotal(httpClientProperties.getMaxTotalConnect())
                    .setMaxConnPerRoute(httpClientProperties.getMaxConnectPerRoute())
                    .build();
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
            factory.setConnectTimeout(httpClientProperties.getConnectTimeout());
            factory.setReadTimeout(httpClientProperties.getReadTimeout());
            factory.setConnectionRequestTimeout(httpClientProperties.getConnectTimeout());
            return factory;
        }
    }

    @Bean
    @ConditionalOnMissingBean(name = {"httpsClientHttpRequestFactory"})
    ClientHttpRequestFactory httpsClientHttpRequestFactory(HttpClientProperties httpClientProperties) {
        if (!httpClientProperties.isUsePool()) {
            HttpsClientRequestFactory factory = new HttpsClientRequestFactory();
            factory.setConnectTimeout(httpClientProperties.getConnectTimeout());
            factory.setReadTimeout(httpClientProperties.getReadTimeout());
            return factory;
        } else {
            CloseableHttpClient httpClient;
            try {
                TrustStrategy acceptingTrustStrategy = (x509Certificates, authType) -> {
                    return true;
                };
                SSLContext sslContext = SSLContexts.custom()
                        .loadTrustMaterial(null, acceptingTrustStrategy)
                        .build();
                SSLConnectionSocketFactory connectionSocketFactory =
                        new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
                HttpClientBuilder httpClientBuilder = HttpClients.custom();
                httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);
                httpClient = httpClientBuilder.build();
            } catch (Exception e) {
                throw new RuntimeException("create httpsClientHttpRequestFactory failed", e);
            }

            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
            factory.setConnectTimeout(httpClientProperties.getConnectTimeout());
            factory.setReadTimeout(httpClientProperties.getReadTimeout());
            factory.setConnectionRequestTimeout(httpClientProperties.getConnectTimeout());
            return factory;
        }
    }
}
