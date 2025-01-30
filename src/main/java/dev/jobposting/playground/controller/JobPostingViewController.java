package dev.jobposting.playground.controller;

import dev.jobposting.playground.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class JobPostingController {
    private final JobPostingService jobPostingService;

    @GetMapping("/")
    public String showJobPostings(Model model) {
        model.addAttribute("jobPostings", jobPostingService.getAllJobPostings());
        return "index";
    }
}
