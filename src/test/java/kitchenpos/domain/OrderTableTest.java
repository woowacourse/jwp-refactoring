package kitchenpos.domain;

import kitchenpos.order.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @Test
    @DisplayName("주문 테이블을 생성할 수 있다.")
    void createOrderTable() {
        // when
        OrderTable orderTable = new OrderTable(1L, null, 0, true);

        // then
        assertThat(orderTable).isNotNull();
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블의 상태를 업데이트할 수 있다.")
    void updateEmpty() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 0, true);

        // when
        orderTable.updateEmpty(false);

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("주문 테이블의 그룹을 업데이트할 수 있다.")
    void updateTableGroup() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 0, true);
        Long tableGroupId = 1L;

        // when
        orderTable.updateTableGroup(tableGroupId);

        // then
        assertThat(orderTable.getTableGroupId()).isEqualTo(tableGroupId);
    }

    @Test
    @DisplayName("주문 테이블에 손님 수를 업데이트할 수 있다.")
    void updateNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 0, false);
        int numberOfGuests = 5;

        // when
        orderTable.updateNumberOfGuests(numberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    @DisplayName("손님 수는 음수일 수 없다.")
    void validateNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 0, false);
        int numberOfGuests = -1;

        // then
        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(numberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
