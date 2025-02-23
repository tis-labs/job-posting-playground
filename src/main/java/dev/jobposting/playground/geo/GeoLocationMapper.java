package dev.jobposting.playground.geo;

public class GeoLocationMapper {

    public static GeoLocation create(double latitude, double longitude) {
        return new GeoLocation(latitude, longitude);
    }
}
