package dev.jobposting.playground.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PublicIpProvider {

    // 현재 내 공인 IP를 조회할 수 있는 외부 API
    private static final String PUBLIC_IP_API = "https://checkip.amazonaws.com";
    private final RestTemplate restTemplate;

    public String getPublicIp() {
        try {
            return restTemplate.getForObject(PUBLIC_IP_API, String.class).trim();
        } catch (Exception e) {
            throw new RuntimeException("공인 IP 조회 실패", e);
        }
    }
}
