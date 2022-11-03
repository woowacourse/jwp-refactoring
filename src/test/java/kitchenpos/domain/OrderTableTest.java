package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("빈 주문테이블인 경우 예외를 발생한다.")
    @Test
    void isEmptyTrue() {
        // given
        OrderTable orderTable = new OrderTable(0, true);

        // when & then
        assertThatThrownBy(orderTable::validateEmpty)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 주문 테이블입니다.");
    }

    @DisplayName("빈 주문테이블이 아닌 경우 예외를 발생하지 않는다.")
    @Test
    void isEmptyFalse() {
        // given
        OrderTable orderTable = new OrderTable(2, false);

        // when & then
        assertDoesNotThrow(orderTable::validateEmpty);
    }

    @DisplayName("상태를 바꿀 때 단체 테이블에 속해있으면 예외를 발생한다.")
    @Test
    void isInGroupWhenChangeEmpty() {
        // given
        OrderTable orderTable = new OrderTable(1L, 2, false);

        // when & then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("TableGroupId가 있습니다.");
    }

    @DisplayName("손님의 수가 0 미만이면 예외를 발생한다")
    @Test
    void numberOfGuestsUnder0ThrowException() {
        // given
        OrderTable orderTable = new OrderTable(1L, 2, false);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuest(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("손님의 수는 0 이상이어야합니다.");
    }
}
