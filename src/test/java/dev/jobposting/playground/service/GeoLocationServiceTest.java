package dev.jobposting.playground.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jobposting.playground.geo.GeoLocation;
import dev.jobposting.playground.geo.GeoLocationService;
import dev.jobposting.playground.network.GeoLocationClient;
import dev.jobposting.playground.network.PublicIpProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeoLocationServiceTest {

    @Mock
    private PublicIpProvider publicIpProvider;

    @Mock
    private GeoLocationClient geoLocationClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private GeoLocationService geoLocationService;

    private String jsonResponse;
    private JsonNode mockJsonNode;

    @BeforeEach
    void setUp() throws Exception {
        jsonResponse = """
            {
              "ip": "8.8.8.8",
              "country": "KR",
              "region": "Gyeonggi-do",
              "city": "Bucheon-si",
              "loc": "37.386,-122.084",
              "timezone": "Asia/Seoul"
            }
        """;

        mockJsonNode = new ObjectMapper().readTree(jsonResponse);
    }

    @Test
    @DisplayName("정상적인 IP로 위치 정보를 조회하면 GeoLocation이 반환된다")
    void getLocationByIp_shouldReturnGeoLocation() throws Exception {
        // given
        String ip = "8.8.8.8";

        when(geoLocationClient.getLocationByIp(ip)).thenReturn(jsonResponse);
        when(objectMapper.readTree(jsonResponse)).thenReturn(mockJsonNode);

        // when
        Optional<GeoLocation> result = geoLocationService.getLocationByIp(ip);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getLatitude()).isEqualTo(37.386);
        assertThat(result.get().getLongitude()).isEqualTo(-122.084);
    }

    @Test
    @DisplayName("Bogon IP 조회 시 Optional.empty()를 반환한다")
    void getLocationByIp_shouldReturnEmptyForBogonIp() {
        // given
        String ip = "192.168.0.1";
        String jsonResponse = """
            {
              "bogon": true
            }
        """;

        when(geoLocationClient.getLocationByIp("https://ipinfo.io/192.168.0.1/json"))
                .thenReturn(jsonResponse);

        // when
        Optional<GeoLocation> result = geoLocationService.getLocationByIp(ip);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("API 요청 실패 시 Optional.empty()를 반환한다")
    void getLocationByIp_shouldReturnEmptyOnApiFailure() {
        // given
        String ip = "invalid-ip";

        when(geoLocationClient.getLocationByIp("https://ipinfo.io/invalid-ip/json"))
                .thenThrow(new RuntimeException("API 호출 실패"));

        // when
        Optional<GeoLocation> result = geoLocationService.getLocationByIp(ip);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("공인 IP 조회가 정상적으로 수행된다")
    void getPublicIp_shouldReturnValidIp() {
        // given
        when(publicIpProvider.getPublicIp()).thenReturn("203.0.113.1");

        // when
        String publicIp = geoLocationService.getPublicIp();

        // then
        assertThat(publicIp).isEqualTo("203.0.113.1");
    }
}
