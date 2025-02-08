package dev.jobposting.playground.controller;

import dev.jobposting.playground.service.JobPostingInfoService;
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

    private final JobPostingInfoService jobPostingInfoService;

    @GetMapping
    public String showJobPostings(Model model) {
        model.addAttribute("jobPostings", jobPostingInfoService.getAllJobPostings());
        return "index";
    }

    @PostMapping("/job/{id}/click")
    public ResponseEntity<Void> increaseClickCount(@PathVariable("id") Long jobId) {
        jobPostingInfoService.increaseClickCount(jobId);
        return ResponseEntity.ok().build();
    }
}
