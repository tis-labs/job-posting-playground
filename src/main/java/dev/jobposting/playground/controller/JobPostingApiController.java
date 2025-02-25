package dev.jobposting.playground.controller;

import dev.jobposting.playground.domain.PaperSize;
import dev.jobposting.playground.service.JobPostingInfoService;
import dev.jobposting.playground.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jobs")
public class JobPostingApiController {
    private final JobPostingService jobPostingService;
    private final JobPostingInfoService jobPostingInfoService;

    @GetMapping
    public ResponseEntity<List<JobCardResponse>> getTopViewedJobs() {
        List<JobPostingResponse> jobPostingsResponses = jobPostingService.findTopViewedJobs();
        List<JobCardResponse> jobCardsResponse = new ArrayList<>();

        for (JobPostingResponse jobPostingResponse : jobPostingsResponses) {
            PaperSize paperSize = PaperSize.getSizeByViews(jobPostingResponse.getTotalViewCount());
            jobCardsResponse.add(JobCardResponse.from(jobPostingResponse, paperSize));
        }

        return ResponseEntity.ok(jobCardsResponse);
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<List<JobCardResponse>> increaseViewCount(@PathVariable("id") Long id) {
        jobPostingInfoService.increaseViewCount(id);

        // 최신 정렬된 공고 리스트 가져오기
        List<JobPostingResponse> sortedJobPostings = jobPostingService.findTopViewedJobs();
        List<JobCardResponse> jobCardsResponse = sortedJobPostings.stream()
                .map(job -> JobCardResponse.from(job, PaperSize.getSizeByViews(job.getTotalViewCount())))
                .toList();

        return ResponseEntity.ok(jobCardsResponse);
    }
}
