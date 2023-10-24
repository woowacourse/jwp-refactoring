package kitchenpos.domain.ordertable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @DisplayName("이미 테이블 그룹에 속한 경우 예외를 발생한다.")
    @Test
    void validate_already_contained() {
        // given
        final OrderTable orderTable = new OrderTable(1L, new TableGroup(2L), 4, false);

        // when
        // then
        assertThatThrownBy(orderTable::validateOrderTableHasTableGroupId)
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 상태를 변경한다.")
    @Test
    void change_empty_status() {
        // given
        final OrderTable orderTable = new OrderTable(1L, new TableGroup(2L), 4, false);

        // when
        orderTable.changeEmptyStatus(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    @Test
    void change_number_of_guests() {
        // given
        final OrderTable orderTable = new OrderTable(1L, new TableGroup(2L), 4, false);
        final int changingNumberOfGuests = 5;

        // when
        orderTable.changeNumberOfGuests(5);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(changingNumberOfGuests);
    }

    @DisplayName("주문 테이블이 비어있으면 손님 수를 변경할 수 없다.")
    @Test
    void change_number_of_guests_fail_with_empty_orderTable() {
        // given
        final OrderTable orderTable = new OrderTable(1L, new TableGroup(2L), 4, true);
        final int changingNumberOfGuests = 5;

        // when
        // then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(changingNumberOfGuests))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문테이블이 비어있는 상태이면 손님 수를 변경할 수 없습니다.");
    }

    @DisplayName("변경할 손님 수가 음수이면 손님 수를 변경할 수 없다.")
    @Test
    void change_number_of_guests_fail_negative_input() {
        // given
        final OrderTable orderTable = new OrderTable(1L, new TableGroup(2L), 4, false);
        final int changingNumberOfGuests = -1;

        // when
        // then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(changingNumberOfGuests))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("변경할 손님 수는 음수이면 안됩니다.");
    }
}
