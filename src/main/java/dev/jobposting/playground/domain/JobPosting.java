package dev.jobposting.playground.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobPosting {
    private String id;
    private int totalViewCount;
    private int currentViewCount;
    private int width;
    private int height;
}
