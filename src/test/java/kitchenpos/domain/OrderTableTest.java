package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("테이블 그룹이 존재하는 테이블은 상태 변경을 할 수 없다.")
    @Test
    void changeTableEmptyStatusIncludedToTableGroup() {
        // arrange
        OrderTable orderTable = new OrderTable(
                1L, 1L, 10, false, List.of()
        );

        // act & assert
        assertThatThrownBy(() -> orderTable.changeEmptyStatus(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("계산이 완료되지 않은 주문이 존재하는 경우 테이블의 상태를 변경할 수 없다.")
    @Test
    void changeTableEmptyStatusHasNotCompletionOrders() {
        // arrange
        List<Order> orders = List.of(
                new Order("COOKING"), new Order("COMPLETION"), new Order("MEAL")
        );
        OrderTable orderTable = new OrderTable(1L, null, 10, false, orders);

        // act & assert
        assertThatThrownBy(() -> orderTable.changeEmptyStatus(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 상태를 변경한다.")
    @Test
    void changeTableEmptyStatus() {
        // arrange
        List<Order> orders = List.of(
                new Order("COMPLETION"), new Order("COMPLETION")
        );
        OrderTable orderTable = new OrderTable(1L, null, 10, false, orders);

        // act
        orderTable.changeEmptyStatus(true);

        // assert
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("방문 손님 수는 음수로 변경할 수 없다.")
    @Test
    void changeTableNumberOfGuestToNegative() {
        // arrange
        OrderTable orderTable = new OrderTable(1L, null, 2, false, List.of());

        // act & assert
        assertThatThrownBy(() -> orderTable.changeNumberOfGuest(-1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("빈 테이블의 방문 손님 수는 변경할 수 없다.")
    @Test
    void changeTableNumberOfGuestForEmptyTable() {
        // arrange
        OrderTable orderTable = new OrderTable(1L, null, 0, true, List.of());

        // act & assert
        assertThatThrownBy(() -> orderTable.changeNumberOfGuest(10))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(0);
    }

    @DisplayName("테이블의 방문 손님 수를 변경한다.")
    @Test
    void changeTableNumberOfGuest() {
        // arrange
        OrderTable orderTable = new OrderTable(1L, null, 2, false, List.of());

        // act
        orderTable.changeNumberOfGuest(10);

        // assert
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
    }
}
