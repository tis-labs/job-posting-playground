package dev.jobposting.playground.network;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "https://ipinfo.io", accept = "application/json")
public interface GeoLocationClient {

    @GetExchange("/{ip}/json")
    String getLocationByIp(@PathVariable("ip") String ip);
}
