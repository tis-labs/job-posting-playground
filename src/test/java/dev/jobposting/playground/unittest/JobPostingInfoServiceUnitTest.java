package dev.jobposting.playground.unittest;

import dev.jobposting.playground.domain.JobPosting;
import dev.jobposting.playground.service.CurrentViewStorage;
import dev.jobposting.playground.service.JobPostingInfoService;
import dev.jobposting.playground.service.TotalViewStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JobPostingInfoServiceUnitTest {
    private JobPostingInfoService service;

    @BeforeEach
    void setUp() {
        service = new JobPostingInfoService(new CurrentViewStorage(), new TotalViewStorage());
        service.resetClickCount();
    }

    @Test
    void 단일_스레드에서_클릭수_증가_테스트() {
        // Given
        JobPostingInfoService service = new JobPostingInfoService(new CurrentViewStorage(), new TotalViewStorage());

        // When
        int firstClick = service.increaseViewCount(1L);
        int doubleClick = service.increaseViewCount(1L);
        int thirdClick = service.increaseViewCount(1L);

        // Then
        assertEquals(1, firstClick);
        assertEquals(2, doubleClick);
        assertEquals(3, thirdClick);
    }

    @Test
    void testIncreaseViewCountMultiThread() throws InterruptedException {
        // Given
        JobPostingInfoService service = new JobPostingInfoService(new CurrentViewStorage(), new TotalViewStorage());
        int threadCount = 6;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // When
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                service.increaseViewCount(1L);
                latch.countDown();
            });
        }

        latch.await(); // 모든 스레드가 끝날 때까지 대기
        executorService.shutdown();

        // Then
        assertEquals(threadCount, service.increaseViewCount(1L) - 1); // 6번 증가했으므로 6이 되어야 함
    }

    @Test
    void 공고_조회시_클릭수_기준으로_정렬되는지_확인() {
        // Given
        service.increaseViewCount(3L);
        service.increaseViewCount(3L);
        service.increaseViewCount(2L);
        service.increaseViewCount(2L);
        service.increaseViewCount(2L);
        service.increaseViewCount(1L);

        // When
        List<JobPosting> sortedJobPostings = service.getAllJobPostings();

        // Then
        List<String> actualSortedIds = sortedJobPostings.stream()
                .map(JobPosting::getId)
                .toList();

        List<String> expectedSortedIds = List.of("2", "3", "1");
        assertEquals(expectedSortedIds, actualSortedIds);
    }
}
