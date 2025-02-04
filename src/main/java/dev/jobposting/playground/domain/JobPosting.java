package dev.jobposting.playground.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobPosting {
    private final Long id;
    private final int clickCount;
}
