package dev.jobposting.playground.repository;

import dev.jobposting.playground.domain.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
}
