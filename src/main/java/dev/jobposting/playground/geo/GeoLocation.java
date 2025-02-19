package dev.jobposting.playground.geo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocation {
    private String ip;
    private String country;
    private String city;
    private String region;
    private String timezone;
    private double latitude;
    private double longitude;
}
