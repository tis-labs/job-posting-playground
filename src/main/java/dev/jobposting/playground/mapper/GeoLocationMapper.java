package dev.jobposting.playground.mapper;

import dev.jobposting.playground.domain.GeoLocation;

public class GeoLocationMapper {

    public static GeoLocation create(String ip, String country, String city, String region, String timezone, double latitude, double longitude) {
        return new GeoLocation(ip, country, city, region, timezone, latitude, longitude);
    }
}
