package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.exception.AlreadyGroupedException;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    void 이미_그룹화_됐으면_그룹화_할때_예외를_발생한다() {
        OrderTable orderTable = new OrderTable(1L, 1L, 2, true);

        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), List.of(orderTable)))
                .isInstanceOf(AlreadyGroupedException.class);
    }
}
