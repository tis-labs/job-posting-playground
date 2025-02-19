package dev.jobposting.playground.geo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jobposting.playground.network.GeoLocationClient;
import dev.jobposting.playground.network.PublicIpProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeoLocationService {

    private final PublicIpProvider publicIpProvider;
    private final GeoLocationClient geoLocationClient;
    private final ObjectMapper objectMapper;

    public Optional<GeoLocation> getLocationByIp(String ip) {
        String processedIp = normalizeIp(ip);

        try {
            String response = geoLocationClient.getLocationByIp(processedIp);
            JsonNode root = objectMapper.readTree(response);

            if (root.has("bogon") && root.get("bogon").asBoolean()) {
                return Optional.empty();
            }

            return Optional.of(parseGeoLocation(root));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String getPublicIp() {
        return publicIpProvider.getPublicIp();
    }

    private String normalizeIp(String ip) {
        // IPv6 로컬 주소 (::1)을 IPv4로 변환
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    private GeoLocation parseGeoLocation(JsonNode root) {
        double[] latLong = extractLatLong(root);
        double latitude = latLong[0];
        double longitude = latLong[1];

        return GeoLocationMapper.create(latitude, longitude);
    }

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
