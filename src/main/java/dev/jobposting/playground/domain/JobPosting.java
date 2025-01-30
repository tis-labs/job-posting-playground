package dev.jobposting.playground.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobPosting {
    private final Long id;
    private final String title;
    private final String company;
    private final String location;
    private final String description;
    private final int clickCount;
}
