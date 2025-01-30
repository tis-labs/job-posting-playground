package dev.jobposting.playground.service;

import dev.jobposting.playground.domain.JobPosting;
import dev.jobposting.playground.dto.JobPostingDto;
import dev.jobposting.playground.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;

    public List<JobPostingDto> getAllJobPostings() {
        return jobPostingRepository.findAll().stream()
                .map(JobPostingDto::fromEntity)
                .collect(Collectors.toList());
    }

    public JobPostingDto increaseClickCount(Long id) {
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("채용 공고를 찾을 수 없음: " + id));

        jobPosting.incrementClickCount();
        jobPostingRepository.save(jobPosting);

        return JobPostingDto.fromEntity(jobPosting);
    }
}
