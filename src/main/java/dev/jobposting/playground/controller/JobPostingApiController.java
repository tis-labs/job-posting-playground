package dev.jobposting.playground.controller;

import dev.jobposting.playground.domain.PaperSize;
import dev.jobposting.playground.event.service.JobEventPostingInfoService;
import dev.jobposting.playground.user.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jobs")
public class JobPostingApiController {
    private final JobEventPostingInfoService jobEventPostingInfoService;

    @GetMapping
    public ResponseEntity<List<JobCardResponse>> getTopViewedJobs() {
        List<JobPostingResponse> jobPostingsResponses = jobEventPostingInfoService.getTopViewedJobs(30);
        List<JobCardResponse> jobCardsResponse = new ArrayList<>();
        for (JobPostingResponse jobPostingResponse : jobPostingsResponses) {
            PaperSize paperSize = PaperSize.getSizeByViews(jobPostingResponse.getTotalViewCount());
            jobCardsResponse.add(JobCardResponse.from(jobPostingResponse, paperSize));
        }
        return ResponseEntity.ok(jobCardsResponse);
    }

    /**
     * TODO: 현재 사용자 클릭수만 반영하도록 반영할 필요 있음
     */
    @PostMapping("/{id}/view")
    public ResponseEntity<List<JobCardResponse>> increaseViewCount(@PathVariable("id") String id, @RequestBody UserRequestDto userRequestDto) {
        jobEventPostingInfoService.click(id, userRequestDto.getId());
        List<JobPostingResponse> jobPostingsResponses = jobEventPostingInfoService.getTopViewedJobs(30);
        List<JobCardResponse> jobCardsResponse = new ArrayList<>();
        for (JobPostingResponse jobPostingResponse : jobPostingsResponses) {
            PaperSize paperSize = PaperSize.getSizeByViews(jobPostingResponse.getTotalViewCount());
            jobCardsResponse.add(JobCardResponse.from(jobPostingResponse, paperSize));
        }
        return ResponseEntity.ok(jobCardsResponse);
    }
}
