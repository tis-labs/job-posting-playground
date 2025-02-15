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
    public ResponseEntity<ViewCountResponse> increaseViewCount(@PathVariable("id") Long id) {
        int updatedCount = jobPostingInfoService.increaseViewCount(id);
        PaperSize paperSize = PaperSize.getSizeByViews(updatedCount);
        ViewCountResponse response = new ViewCountResponse("조회수 증가", updatedCount, paperSize.getWidth(), paperSize.getHeight());
        return ResponseEntity.ok(response);
    }
}
