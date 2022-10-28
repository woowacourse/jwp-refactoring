package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Order 도메인 테스트")
class OrderTableTest {

    @DisplayName("테이블 손님의 수 변경 시 손님의 수는 0보다 커야한다")
    @Test
    void changeNumberOfGuestsNumberOfGuestsIsLowerZero() {
        int invalidNumberOfGuests = -1;

        final OrderTable orderTable = new OrderTable(1, false);
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(invalidNumberOfGuests))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("손님의 수는 0명 이하일 수 없습니다.");
    }

    @DisplayName("테이블 손님의 수 변경 시 테이블이 비어있으면 안된다")
    @Test
    void changeNumberOfGuestsOrderTableIsEmpty() {
        final OrderTable orderTable = new OrderTable(0, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
