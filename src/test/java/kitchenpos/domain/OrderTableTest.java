package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import kitchenpos.domain.ordertable.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTableTest {

    @Test
    @DisplayName("테이블 그룹이 존재할 경우 테이블의 비운상태를 수정하면 예외가 발생한다.")
    void changeEmptyWithTableGroup() {
        final OrderTable orderTable = new OrderTable(1L, 10, true);
        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹이 있다면 주문 테이블의 비운 상태를 수정할 수 없습니다.");
    }

    @ParameterizedTest(name = "주문상태가 {0} 일 경우 테이블의 비운상태를 수정하면 예외가 발생한다.")
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmptyWithInvalidOrderStatus(final String orderStatus) {
        final OrderTable orderTable = new OrderTable(1L, null, 10, true, OrderStatus.from(orderStatus));
        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리중이거나 식사중이면 주문 테이블의 비운 상태를 수정할 수 없습니다.");
    }

    @Test
    @DisplayName("주문 테이블이 비어있을 경우 테이블의 guest 수를 변경하면 예외가 발생한다.")
    void changeNumberOfGuestsWithEmptyOrderTable() {
        final OrderTable orderTable = new OrderTable(1L, null, 10, true);
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 비어있으면 손님 수를 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("주문 테이블의 손님수를 0으로 바꿀 수 있다.")
    void changeNumberOfGuestsZero() {
        final OrderTable orderTable = new OrderTable(1L, null, 10, false);
        assertDoesNotThrow(() -> orderTable.changeNumberOfGuests(0));
    }

    @ParameterizedTest(name = "주문 테이블의 손님수를 음수 {0} 로 변경할 경우 예외가 발생한다.")
    @ValueSource(ints = {-1, -100, -1000})
    void changeNumberOfGuestsWithInvalidNumberOfGuests(final int numberOfGuests) {
        final OrderTable orderTable = new OrderTable(1L, null, 10, false);
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(numberOfGuests))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블의 손님 수는 음수로 변경할 수 없습니다.");
    }
}
