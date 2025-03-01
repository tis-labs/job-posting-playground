package dev.jobposting.playground.unittest;

import dev.jobposting.playground.domain.JobPosting;
import dev.jobposting.playground.service.CurrentViewStorage;
import dev.jobposting.playground.service.JobPostingInfoService;
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
        service = new JobPostingInfoService(new CurrentViewStorage());
        service.resetClickCount(); // 클릭 수 초기화
    }

    @Test
    void 단일_스레드에서_클릭수_증가_테스트() {
        // Given (테스트를 위한 서비스 인스턴스 생성)
        JobPostingInfoService service = new JobPostingInfoService(new CurrentViewStorage());

        // When (같은 jobId에 대해 여러 번 클릭 증가)
        int firstClick = service.increaseViewCount(1L);
        int doubleClick = service.increaseViewCount(1L);
        int thirdClick = service.increaseViewCount(1L);

        // Then (클릭 수가 정상적으로 증가하는지 확인)
        assertEquals(1, firstClick);
        assertEquals(2, doubleClick);
        assertEquals(3, thirdClick);
    }

    @Test
    void testIncreaseViewCountMultiThread() throws InterruptedException {
        // Given
        JobPostingInfoService service = new JobPostingInfoService(new CurrentViewStorage());
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
        // Given: 여러 공고에 대해 클릭 수 추가
        service.increaseViewCount(3L);
        service.increaseViewCount(3L);
        service.increaseViewCount(2L);
        service.increaseViewCount(2L);
        service.increaseViewCount(2L);
        service.increaseViewCount(1L);

        // When: 정렬된 공고 리스트 조회
        List<JobPosting> sortedJobPostings = service.getAllJobPostings();

        // Then: 정렬된 ID 리스트를 예상 값과 비교
        List<Long> actualSortedIds = sortedJobPostings.stream()
                .map(JobPosting::getId)
                .toList();

        List<Long> expectedSortedIds = List.of(2L, 3L, 1L); // 예상 정렬 결과
        assertEquals(expectedSortedIds, actualSortedIds);
    }
}
