package dev.jobposting.playground.network;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PublicIpProvider {

    private final PublicIpClient publicIpClient;

    public String getPublicIp() {
        try {
            return publicIpClient.getPublicIp().trim();
        } catch (Exception e) {
            throw new RuntimeException("공인 IP 조회 실패", e);
        }
    }
}
