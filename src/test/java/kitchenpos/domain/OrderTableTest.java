package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("주문 테이블이 기존 테이블 그룹에 포함되어 있는 경우 예외 발생")
    void whenOrderTableIsIncludeInTableGroup() {
        final OrderTable orderTable = new OrderTable(1L, 2L, 3, false);

        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("손님의 수가 음수이면 예외 발생")
    void whenNumberOfGuestsIsNegative() {
        final OrderTable orderTable = new OrderTable(1L, 1L, 3, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블일 경우 예외 발생")
    void whenOrderTableIsEmpty() {
        final OrderTable orderTable = new OrderTable(1L, 1L, 3, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(3))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
