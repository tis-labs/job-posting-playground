package dev.jobposting.playground.controller;

import dev.jobposting.playground.dto.JobPostingDto;
import dev.jobposting.playground.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class JobPostingController {
    private final JobPostingService jobPostingService;

    @GetMapping("/")
    public String showJobPostings(Model model) {
        model.addAttribute("jobPostings", jobPostingService.getAllJobPostings());
        return "index";
    }

    @PostMapping("/api/job-postings/{id}/click")
    public ResponseEntity<JobPostingDto> increaseClickCount(@PathVariable("id") Long id) {
        JobPostingDto updatedPosting = jobPostingService.increaseClickCount(id);
        return ResponseEntity.ok(updatedPosting);
    }
}
