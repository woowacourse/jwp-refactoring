package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("테이블 그룹이 존재하는 테이블은 상태 변경을 할 수 없다.")
    @Test
    void changeTableEmptyStatusIncludedToTableGroup() {
        // arrange
        OrderTable orderTable = createGroupedOrderTable(1L, 1L);

        // act & assert
        assertThatThrownBy(() -> orderTable.changeEmptyStatus(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("계산이 완료되지 않은 주문이 존재하는 경우 테이블의 상태를 변경할 수 없다.")
    @Test
    void changeTableEmptyStatusHasNotCompletionOrders() {
        // arrange
        OrderTable orderTable = createOrderTable(1L, 0, "COOKING", "COMPLETION", "MEAL");

        // act & assert
        assertThatThrownBy(() -> orderTable.changeEmptyStatus(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 상태를 변경한다.")
    @Test
    void changeTableEmptyStatus() {
        // arrange
        OrderTable orderTable = createOrderTable(1L, 0, "COMPLETION", "COMPLETION");

        // act
        orderTable.changeEmptyStatus(true);

        // assert
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("방문 손님 수는 음수로 변경할 수 없다.")
    @Test
    void changeTableNumberOfGuestToNegative() {
        // arrange
        OrderTable orderTable = createOrderTable(1L, 0);

        // act & assert
        assertThatThrownBy(() -> orderTable.changeNumberOfGuest(-1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(0);
    }

    @DisplayName("빈 테이블의 방문 손님 수는 변경할 수 없다.")
    @Test
    void changeTableNumberOfGuestForEmptyTable() {
        // arrange
        OrderTable orderTable = createEmptyOrderTable(1L);

        // act & assert
        assertThatThrownBy(() -> orderTable.changeNumberOfGuest(10))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(0);
    }

    @DisplayName("테이블의 방문 손님 수를 변경한다.")
    @Test
    void changeTableNumberOfGuest() {
        // arrange
        OrderTable orderTable = createOrderTable(1L, 0);

        // act
        orderTable.changeNumberOfGuest(10);

        // assert
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("계산이 완료되지 않은 주문이 있는 경우 그룹 해제가 불가능하다.")
    @Test
    void ungroupHasNotCompletionOrderTable() {
        // arrange
        OrderTable orderTable = createGroupedOrderTable(1L, 1L, "COOKING", "COMPLETION", "MEAL");

        // act
        assertThatThrownBy(() -> orderTable.leaveGroup())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 그룹을 해제한다.")
    @Test
    void ungroupOrderTables() {
        // arrange
        OrderTable orderTable = createGroupedOrderTable(1L, 1L, "COMPLETION", "COMPLETION");

        // act
        orderTable.leaveGroup();

        // assert
        assertThat(orderTable.getTableGroupId()).isNull();
        assertThat(orderTable.isEmpty()).isFalse();
    }

    private OrderTable createGroupedOrderTable(final Long id, final Long groupId, final String... orderStatus) {
        List<Order> orders = Stream.of(orderStatus)
                .map(Order::new)
                .collect(Collectors.toList());

        return new OrderTable(id, groupId, 0, false, orders);
    }

    private OrderTable createOrderTable(final Long id, int numberOfGuests, final String... orderStatus) {
        List<Order> orders = Stream.of(orderStatus)
                .map(Order::new)
                .collect(Collectors.toList());

        return new OrderTable(id, null, numberOfGuests, false, orders);
    }

    private OrderTable createEmptyOrderTable(final Long id) {
        return new OrderTable(id, null, 0, true, List.of());
    }
}
