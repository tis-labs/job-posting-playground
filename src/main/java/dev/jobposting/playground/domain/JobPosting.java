package dev.jobposting.playground.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobPosting {
    private Long id;
    private int totalViewCount;
    private int fiveMinViewCount;
    private int width;
    private int height;
}
