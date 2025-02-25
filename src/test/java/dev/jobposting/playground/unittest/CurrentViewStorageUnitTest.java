package dev.jobposting.playground.unittest;

import dev.jobposting.playground.service.CurrentViewStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrentViewStorageUnitTest {
    private CurrentViewStorage currentViewStorage;

    @BeforeEach
    void setUp() {
        currentViewStorage = new CurrentViewStorage();
    }

    @Test
    void 최근_클릭_최대_5개_유지_확인() {

        // given: 6개의 클릭 추가 (jobId: "1"~"6")
        for (int i = 1; i <= 6; i++) {
            currentViewStorage.increase(String.valueOf(i));
        }

        // when: 현재 저장된 jobId 조회
        List<String> topJobs = currentViewStorage.getTopCurrentViewedJobs();

        // then: 가장 오래된 jobId("1")가 삭제되고, "2"~"6"만 남아야 함
        assertThat(topJobs).containsExactly("2", "3", "4", "5", "6");
    }

    @Test
    void 클릭_횟수가_같을_경우_정확한_순서로_정렬됨() {

        // given: 최대 크기(5개) 초과하지 않도록 클릭 추가
        currentViewStorage.increase("1"); // 1번 클릭
        currentViewStorage.increase("2"); // 1번 클릭
        currentViewStorage.increase("3"); // 1번 클릭
        currentViewStorage.increase("4"); // 1번 클릭
        currentViewStorage.increase("5"); // 1번 클릭
        currentViewStorage.increase("3"); // "3" 2회
        currentViewStorage.increase("5"); // "5" 2회
        currentViewStorage.increase("5"); // "5" 3회

        // when: 정렬된 jobId 조회
        List<String> topJobs = currentViewStorage.getTopCurrentViewedJobs();

        // then: 클릭 횟수 내림차순 정렬 + FIFO 유지
        assertThat(topJobs).containsExactly("5", "4", "3");
    }


    @Test
    void 클릭_횟수_기반_정렬_확인() {

        // given: 특정 jobId("3", "3", "2", "2", "2", "1")를 클릭
        currentViewStorage.increase("3");
        currentViewStorage.increase("3");
        currentViewStorage.increase("2");
        currentViewStorage.increase("2");
        currentViewStorage.increase("2");
        currentViewStorage.increase("1");

        // when: 정렬된 jobId 조회
        List<String> topJobs = currentViewStorage.getTopCurrentViewedJobs();

        // then: 가장 많이 클릭된 "2"번이 가장 앞에 와야 함
        assertThat(topJobs).containsExactly("2", "3", "1");
    }

    @Test
    void 특정_공고의_클릭_횟수_확인() {

        // given: jobId "3"을 4번 클릭
        currentViewStorage.increase("3");
        currentViewStorage.increase("3");
        currentViewStorage.increase("3");
        currentViewStorage.increase("3");

        // when: 특정 jobId의 클릭 수 조회
        int clickCount = currentViewStorage.getClickCount("3");

        // then: 클릭 수가 4여야 함
        assertThat(clickCount).isEqualTo(4);
    }

    @Test
    void 존재하지_않는_공고의_클릭_횟수_조회시_0_반환() {

        // when: 아직 클릭된 적 없는 jobId("99") 조회
        int clickCount = currentViewStorage.getClickCount("99");

        // then: 클릭 수는 0이어야 함
        assertThat(clickCount).isEqualTo(0);
    }

    @Test
    void 최대_5개_초과_시_오래된_데이터_삭제되지만_공고는_유지됨() {

        // given: 클릭 데이터를 초과해서 추가
        currentViewStorage.increase("2");
        currentViewStorage.increase("3");
        currentViewStorage.increase("3");
        currentViewStorage.increase("5");
        currentViewStorage.increase("5");
        currentViewStorage.increase("6"); // "2"가 제거됨

        // when: 정렬된 jobId 조회
        List<String> topJobs = currentViewStorage.getTopCurrentViewedJobs();

        // then: "2"가 제거되고, 클릭 횟수 기준으로 정렬
        assertThat(topJobs).containsExactly("3", "5", "6");
    }

    @Test
    void 클릭_횟수가_같을_경우_최근_클릭된_순서대로_정렬됨() {

        // given: 동일한 횟수의 클릭 추가
        currentViewStorage.increase("3");
        currentViewStorage.increase("3");
        currentViewStorage.increase("3");
        currentViewStorage.increase("5");
        currentViewStorage.increase("5");
        currentViewStorage.increase("6"); // "3"이 제거됨
        currentViewStorage.increase("6"); // "3"이 제거됨
        currentViewStorage.increase("6"); // "3"이 제거됨

        // when: 정렬된 jobId 조회
        List<String> topJobs = currentViewStorage.getTopCurrentViewedJobs();

        // then: "6"이 가장 많이 클릭되었으므로, 정렬 순서 확인
        assertThat(topJobs).containsExactly("6", "5");
    }

    @Test
    void 삭제된_후_다시_클릭하면_클릭_횟수_기준으로_정렬됨() {

        // given: 5개 공고 클릭
        currentViewStorage.increase("1");
        currentViewStorage.increase("2");
        currentViewStorage.increase("3");
        currentViewStorage.increase("4");
        currentViewStorage.increase("5");

        // "1"이 제거될 만큼 추가 클릭
        currentViewStorage.increase("6");
        currentViewStorage.increase("7");
        currentViewStorage.increase("8");

        // when: "1"을 다시 3번 클릭
        currentViewStorage.increase("1");
        currentViewStorage.increase("1");
        currentViewStorage.increase("1");

        // 정렬된 jobId 조회
        List<String> topJobs = currentViewStorage.getTopCurrentViewedJobs();

        // then: "1"이 3회 클릭되어 가장 앞쪽에 위치해야 함
        assertThat(topJobs).containsExactly("1", "7", "8");
    }

    @Test
    void 여러_공고를_순차적으로_클릭하면서_정렬이_유지되는지_확인() {

        // given: 초기 데이터
        currentViewStorage.increase("2");
        currentViewStorage.increase("3");
        currentViewStorage.increase("3");
        currentViewStorage.increase("5");
        currentViewStorage.increase("5");

        // when: 새로운 클릭 추가
        currentViewStorage.increase("6"); // "2"가 삭제됨
        currentViewStorage.increase("6"); // "3"이 삭제됨
        currentViewStorage.increase("6"); // "3"이 삭제됨

        // 현재 상태
        List<String> topJobs = currentViewStorage.getTopCurrentViewedJobs();

        // then: 가장 많이 클릭된 순서대로 정렬됨
        assertThat(topJobs).containsExactly("6", "5");
    }
}
