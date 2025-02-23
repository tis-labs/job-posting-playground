package dev.jobposting.playground.network;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpClientConfig {

    @Bean
    public GeoLocationClient geoLocationClient(HttpServiceProxyFactory httpServiceProxyFactory) {
        return httpServiceProxyFactory.createClient(GeoLocationClient.class);
    }

    @Bean
    public PublicIpClient publicIpClient(HttpServiceProxyFactory httpServiceProxyFactory) {
        return httpServiceProxyFactory.createClient(PublicIpClient.class);
    }

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory() {
        RestClient restClient = RestClient.builder().build();
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
    }
}
