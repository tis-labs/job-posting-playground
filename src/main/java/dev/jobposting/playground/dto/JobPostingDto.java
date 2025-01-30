package dev.jobposting.playground.dto;

import dev.jobposting.playground.domain.JobPosting;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobPostingDto {
    private final Long id;
    private final String title;
    private final String company;
    private final String location;
    private final String description;
    private final int clickCount;

    public static JobPostingDto fromEntity(JobPosting jobPosting) {
        return JobPostingDto.builder()
                .id(jobPosting.getId())
                .title(jobPosting.getTitle())
                .company(jobPosting.getCompany())
                .location(jobPosting.getLocation())
                .description(jobPosting.getDescription())
                .clickCount(jobPosting.getClickCount())
                .build();
    }
}
