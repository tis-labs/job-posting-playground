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

        try {
            Optional<GeoLocation> geoLocation = geoLocationService.getLocationByIp(ip);
            if (geoLocation.isPresent()) {
                GeoLocation location = geoLocation.get();
                httpRequest.setAttribute("latitude", location.getLatitude());
                httpRequest.setAttribute("longitude", location.getLongitude());
            }
        } catch (Exception e) {
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류");
            return;
        }

        chain.doFilter(request, response);
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
