package dev.jobposting.playground.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jobposting.playground.mapper.GeoLocationMapper;
import dev.jobposting.playground.util.PublicIpProvider;
import dev.jobposting.playground.domain.GeoLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeoLocationService {

    private static final String GEO_API_URL = "https://ipinfo.io/%s/json";

    private final PublicIpProvider publicIpProvider;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    public Optional<GeoLocation> getLocationByIp(String ip) {
        String processedIp = normalizeIp(ip);
        String url = String.format(GEO_API_URL, processedIp);

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            // "bogon": true 이면 위치 정보 없다는 뜻.
            if (root.has("bogon") && root.get("bogon").asBoolean()) {
                return Optional.empty();
            }

            return Optional.of(parseGeoLocation(root));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * 현재 서버의 공인 IP 조회
     */
    public String getPublicIp() {
        return publicIpProvider.getPublicIp();
    }

    /**
     * IPv6 로컬 주소 (::1)을 IPv4로 변환
     */
    private String normalizeIp(String ip) {
        // IPv6 로컬 주소 (::1)을 IPv4로 변환
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    private GeoLocation parseGeoLocation(JsonNode root) {
        // IP, 국가, 도시 정보 추출
        String ip = root.path("ip").asText("Unknown");
        String country = root.path("country").asText("Unknown");
        String city = root.path("city").asText("Unknown");
        String region = root.path("region").asText("Unknown");
        String timezone = root.path("timezone").asText("Unknown");

        // 위도 & 경도 추출
        double[] latLong = extractLatLong(root);
        double latitude = latLong[0];
        double longitude = latLong[1];

        return GeoLocationMapper.create(ip, country, city, region, timezone, latitude, longitude);
    }

    /**
     * JSON 응답에서 위도 & 경도 추출
     */
    private double[] extractLatLong(JsonNode root) {
        double latitude = 0.0;
        double longitude = 0.0;

        String loc = root.path("loc").asText(); // "37.386,-122.084"
        if (loc.contains(",")) {
            String[] latLong = loc.split(",");
            try {
                latitude = Double.parseDouble(latLong[0]);
                longitude = Double.parseDouble(latLong[1]);
            } catch (NumberFormatException e) {
            }
        }
        return new double[]{latitude, longitude};
    }
}
