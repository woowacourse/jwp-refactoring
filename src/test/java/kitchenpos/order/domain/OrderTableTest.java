package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("주문 테이블이 empty값을 변경할 수 있다.")
    void changeEmpty() {
        final OrderTable orderTable = new OrderTable(1L, null, 0, false);

        orderTable.changeEmpty(true);

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블이 기존 테이블 그룹에 포함되어 있는 경우 예외 발생")
    void whenOrderTableIsIncludeInTableGroup() {
        final OrderTable orderTable = new OrderTable(1L, 2L, 3, false);

        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("손님의 수가 정상적으로 변경된다.")
    void changeNumberOfGuests() {
        final OrderTable orderTable = new OrderTable(1L, 1L, 0, false);

        orderTable.changeNumberOfGuests(5);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
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

    @Test
    @DisplayName("주문 테이블을 빈 테이블로 변경한다.")
    void cha() {
        final OrderTable orderTable = new OrderTable(1L, 1L, 0, true);

        final OrderTable changeEmptyTable = orderTable.changeEmptyTable();

        assertAll(
                () -> assertThat(changeEmptyTable.getTableGroupId()).isNull(),
                () -> assertThat(changeEmptyTable.isEmpty()).isFalse()
        );
    }
}
