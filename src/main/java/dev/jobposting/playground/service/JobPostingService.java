package dev.jobposting.playground.service;

import dev.jobposting.playground.domain.JobPosting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostingService {
    public List<JobPosting> getAllJobPostings() {
        return Arrays.asList(
                JobPosting.builder().id(1L).title(" ").company(" ").location(" ").description(" ").clickCount(0).build(),
                JobPosting.builder().id(2L).title(" ").company(" ").location(" ").description(" ").clickCount(1).build(),
                JobPosting.builder().id(3L).title(" ").company(" ").location(" ").description(" ").clickCount(2).build(),
                JobPosting.builder().id(4L).title(" ").company(" ").location(" ").description(" ").clickCount(3).build(),
                JobPosting.builder().id(5L).title(" ").company(" ").location(" ").description(" ").clickCount(4).build(),
                JobPosting.builder().id(6L).title(" ").company(" ").location(" ").description(" ").clickCount(5).build()
        );
    }
}
