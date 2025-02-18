package dev.jobposting.playground.controller;

import dev.jobposting.playground.domain.GeoLocation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/geo")
public class GeoLocationController {

    @GetMapping
    public GeoLocation getGeoLocation(GeoLocation geoLocation) {
        return geoLocation;
    }
}
