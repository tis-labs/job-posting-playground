package dev.jobposting.playground.config;

import dev.jobposting.playground.resolver.GeoLocationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class GeoLocationResolverConfig implements WebMvcConfigurer {

    private final GeoLocationResolver geoLocationResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(geoLocationResolver);
    }
}
