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

        // given
        for (int i = 1; i <= 6; i++) {
            currentViewStorage.increase(String.valueOf(i));
        }

        // when
        List<String> topJobs = currentViewStorage.getTopCurrentViewedJobs();

        // then
        assertThat(topJobs).containsExactly("2", "3", "4", "5", "6");
    }

    @Test
    void 특정_공고의_클릭_횟수_확인() {

        // given
        currentViewStorage.increase("3");
        currentViewStorage.increase("3");
        currentViewStorage.increase("3");
        currentViewStorage.increase("3");

        // when
        int clickCount = currentViewStorage.getClickCount("3");

        // then
        assertThat(clickCount).isEqualTo(4);
    }

    @Test
    void 존재하지_않는_공고의_클릭_횟수_조회시_0_반환() {

        // when
        int clickCount = currentViewStorage.getClickCount("99");

        // then
        assertThat(clickCount).isEqualTo(0);
    }

    @Test
    void 최대_5개_초과_시_오래된_데이터_삭제되지만_공고는_유지됨() {

        // given
        currentViewStorage.increase("2");
        currentViewStorage.increase("3");
        currentViewStorage.increase("3");
        currentViewStorage.increase("5");
        currentViewStorage.increase("5");
        currentViewStorage.increase("6"); // "2"가 제거됨

        // when
        List<String> topJobs = currentViewStorage.getTopCurrentViewedJobs();

        // then
        assertThat(topJobs).containsExactly("3", "5", "6");
    }

    @Test
    void 클릭_횟수가_같을_경우_최근_클릭된_순서대로_정렬됨() {

        // given
        currentViewStorage.increase("3");
        currentViewStorage.increase("3");
        currentViewStorage.increase("3");
        currentViewStorage.increase("5");
        currentViewStorage.increase("5");
        currentViewStorage.increase("6"); // "3"이 제거됨
        currentViewStorage.increase("6"); // "3"이 제거됨
        currentViewStorage.increase("6"); // "3"이 제거됨

        // when
        List<String> topJobs = currentViewStorage.getTopCurrentViewedJobs();

        // then
        assertThat(topJobs).containsExactly("6", "5");
    }

    @Test
    void 삭제된_후_다시_클릭하면_클릭_횟수_기준으로_정렬됨() {

        // given
        currentViewStorage.increase("1");
        currentViewStorage.increase("2");
        currentViewStorage.increase("3");
        currentViewStorage.increase("4");
        currentViewStorage.increase("5");

        currentViewStorage.increase("6");
        currentViewStorage.increase("7");
        currentViewStorage.increase("8");

        // when
        currentViewStorage.increase("1");
        currentViewStorage.increase("1");
        currentViewStorage.increase("1");

        List<String> topJobs = currentViewStorage.getTopCurrentViewedJobs();

        // then
        assertThat(topJobs).containsExactly("1", "7", "8");
    }

    @Test
    void 여러_공고를_순차적으로_클릭하면서_정렬이_유지되는지_확인() {

        // given
        currentViewStorage.increase("2");
        currentViewStorage.increase("3");
        currentViewStorage.increase("3");
        currentViewStorage.increase("5");
        currentViewStorage.increase("5");

        // when
        currentViewStorage.increase("6"); // "2"가 삭제됨
        currentViewStorage.increase("6"); // "3"이 삭제됨
        currentViewStorage.increase("6"); // "3"이 삭제됨

        List<String> topJobs = currentViewStorage.getTopCurrentViewedJobs();

        // then
        assertThat(topJobs).containsExactly("6", "5");
    }
}
