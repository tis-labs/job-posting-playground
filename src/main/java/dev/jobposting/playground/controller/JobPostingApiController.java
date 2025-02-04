package dev.jobposting.playground.controller;

import dev.jobposting.playground.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/job-postings")
@RequiredArgsConstructor
class JobPostingApiController {
    private final JobPostingService jobPostingService;

    @PostMapping("/click")
    public void increaseClickCount(@RequestParam("jobId") Long jobId) {
        jobPostingService.increaseClickCount(jobId);
    }
}
