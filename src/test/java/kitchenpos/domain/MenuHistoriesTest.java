package kitchenpos.domain;

import kitchenpos.domain.history.MenuHistories;
import kitchenpos.domain.history.MenuHistory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuHistoriesTest {

    @DisplayName("특정 메뉴 금액 변경 내역 중 조회 시간 이전의 시간에서 일어난 변경 중 최신을 가져온다")
    @Test
    void getLatestPrice() {
        LocalDateTime baseTime = LocalDateTime.now();
        List<MenuHistory> totalMenuHistories = List.of(
                new MenuHistory(1L, 1000L, "test1-1", baseTime.minusSeconds(10)),
                new MenuHistory(1L, 900L, "test1-1", baseTime.minusSeconds(5)),
                new MenuHistory(1L, 2000L, "test1-1", baseTime),
                new MenuHistory(2L, 1000L, "test1-1", baseTime.minusSeconds(20))
        );
        MenuHistories menuHistories = new MenuHistories(totalMenuHistories);

        Long latestPrice = menuHistories.getLatestPrice(1L, LocalDateTime.now());

        assertThat(latestPrice).isEqualTo(2000L);
    }

    @DisplayName("특정 메뉴 이름 변경 내역 중 조회 시간 이전의 시간에서 일어난 변경 중 최신을 가져온다")
    @Test
    void getLatestName() {
        LocalDateTime baseTime = LocalDateTime.now();
        List<MenuHistory> totalMenuHistories = List.of(
                new MenuHistory(1L, 1000L, "test1-1", baseTime.minusSeconds(10)),
                new MenuHistory(1L, 1000L, "test1-2", baseTime.minusSeconds(5)),
                new MenuHistory(1L, 1000L, "test1-3", baseTime),
                new MenuHistory(2L, 1000L, "test2-1", baseTime.minusSeconds(20))
        );
        MenuHistories menuHistories = new MenuHistories(totalMenuHistories);

        String latestName = menuHistories.getLatestName(1L, LocalDateTime.now());

        assertThat(latestName).isEqualTo("test1-3");
    }
}
