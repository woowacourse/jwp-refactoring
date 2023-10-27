package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @DisplayName("현재 날짜로 그룹을 생성한다.")
    @Test
    void addOrderTables() {
        // given
        final LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

        // when, then
        assertDoesNotThrow(() -> new TableGroup(time));
    }
}
