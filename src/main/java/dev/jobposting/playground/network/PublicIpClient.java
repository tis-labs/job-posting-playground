package dev.jobposting.playground.network;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("https://checkip.amazonaws.com")
public interface PublicIpClient {

    @GetExchange
    String getPublicIp();
}
