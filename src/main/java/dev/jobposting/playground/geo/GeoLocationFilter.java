package dev.jobposting.playground.geo;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GeoLocationFilter implements Filter {

    private final GeoLocationService geoLocationService;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String ip = getClientIp(httpRequest);
        Optional<GeoLocation> geoLocation = geoLocationService.getLocationByIp(ip);

        if (geoLocation.isPresent()) {
            GeoLocation location = geoLocation.get();
            request.setAttribute("latitude", location.getLatitude());
            request.setAttribute("longitude", location.getLongitude());
        } else {
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "위치를 찾을 수 없음");
            return;
        }

        chain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            ip = geoLocationService.getPublicIp();
        }
        return ip;
    }
}
