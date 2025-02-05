package dev.jobposting.playground.controller;

import dev.jobposting.playground.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @GetMapping
    public String showJobPostings(Model model) {
        model.addAttribute("jobPostings", jobPostingService.getAllJobPostings());
        return "index";
    }

    @PostMapping("/job/{id}/click")
    public ResponseEntity<Void> increaseClickCount(@PathVariable("id") Long jobId) {
        jobPostingService.increaseClickCount(jobId);
        return ResponseEntity.ok().build();
    }
}
