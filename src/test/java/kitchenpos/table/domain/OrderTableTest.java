package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {
    @DisplayName("changeNumberOfGuests: 테이블이 비어있는 상태에서 인원수를 변경하려고 하면 예외")
    @Test
    void changeNumberOfGuests1() {
        final OrderTable orderTable = new OrderTable(0, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블이 비어있는 상태에서는 인원을 변경할 수 없습니다.");
    }

    @DisplayName("changeNumberOfGuests: 0 미만의 수로 변경하려고 하면 예외처리")
    @Test
    void name() {
        final OrderTable orderTable = new OrderTable(0, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("손님 수가 0보다 작을수 없습니다.");
    }
}
