package dev.jobposting.playground.user.config;

import java.util.Optional;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import dev.jobposting.playground.geo.GeoLocation;
import dev.jobposting.playground.geo.GeoLocationService;
import dev.jobposting.playground.user.dto.UserLocationDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
	private final GeoLocationService geoLocationService;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(UserLocation.class) != null
			&& parameter.getParameterType().equals(UserLocationDto.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory) throws Exception {

		HttpServletRequest httpRequest = (HttpServletRequest) webRequest.getNativeRequest();
		String ip = getClientIp(httpRequest);
		Optional<GeoLocation> geoLocation = geoLocationService.getLocationByIp(ip);
		if (geoLocation.isPresent()) {
			GeoLocation location = geoLocation.get();
			return new UserLocationDto(location.getLatitude(), location.getLongitude());
		}
		return null;
	}

	private String getClientIp(HttpServletRequest request) {
		String ip = request.getRemoteAddr();

		if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
			ip = "127.0.0.1";
		}

		if ("127.0.0.1".equals(ip)) {
			ip = geoLocationService.getPublicIp();
		}
		return ip;
	}
}
