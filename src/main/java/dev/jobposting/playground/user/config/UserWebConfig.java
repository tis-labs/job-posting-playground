package dev.jobposting.playground.user.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dev.jobposting.playground.geo.GeoLocationService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UserWebConfig implements WebMvcConfigurer {
	private final GeoLocationService geoLocationService;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new UserArgumentResolver(geoLocationService));
	}
}
