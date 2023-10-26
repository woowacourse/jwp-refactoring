package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("주문 테이블을 빈 상태로 변경한다.")
    @Test
    void changeEmpty() {
        // given
        final OrderTable orderTable = new OrderTable(1L, 0, false, List.of(
            new Order(1L, OrderStatus.COMPLETION, List.of(new OrderLineItem(1L, 1L, "치킨", new MenuPrice(BigDecimal.TEN), null)))));

        // when
        orderTable.changeEmpty(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("주문이 완료되지 않은 테이블을 빈 상태로 변경하려 시도하면 예외가 발생한다.")
    @Test
    void changeEmpty_failProceedingTable() {
        // given
        final OrderTable orderTable = new OrderTable(1L, 0, false, List.of(
            new Order(1L, OrderStatus.COOKING, List.of(new OrderLineItem(1L, 1L, "치킨", new MenuPrice(BigDecimal.TEN), null)))));

        // when
        // then
        assertThatThrownBy(() -> orderTable.changeEmpty(true)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블에 주문을 추가한다.")
    @Test
    void addOrder() {
        // given
        final OrderTable orderTable = new OrderTable(1L, 0, false, List.of(
            new Order(1L, OrderStatus.COOKING, List.of(new OrderLineItem(1L, 1L, "치킨", new MenuPrice(BigDecimal.TEN), null)))));

        // when
        final Order addedOrder = new Order(2L, OrderStatus.COOKING,
                                           List.of(new OrderLineItem(1L, 1L, "치킨", new MenuPrice(BigDecimal.TEN), null)));
        orderTable.addOrder(addedOrder);

        // then
        assertThat(orderTable.getOrders()).contains(addedOrder);
    }

    @DisplayName("빈 테이블에 주문을 추가하면 예외가 발생한다.")
    @Test
    void addOrder_failEmptyTable() {
        // given
        final OrderTable orderTable = new OrderTable(1L, 0, true, List.of(
            new Order(1L, OrderStatus.COOKING, List.of(new OrderLineItem(1L, 1L, "치킨", new MenuPrice(BigDecimal.TEN), null)))));

        // when
        // then
        assertThatThrownBy(
            () -> orderTable.addOrder(
                new Order(2L, OrderStatus.COOKING, List.of(new OrderLineItem(1L, 1L, "치킨", new MenuPrice(BigDecimal.TEN), null)))));
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = new OrderTable(1L, 1, false, List.of(
            new Order(1L, OrderStatus.COOKING, List.of(new OrderLineItem(1L, 1L, "치킨", new MenuPrice(BigDecimal.TEN), null)))));

        // when
        final int changedNumberOfGuests = 5;
        orderTable.changeNumberOfGuests(changedNumberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(changedNumberOfGuests);
    }

    @DisplayName("빈 테이블의 손님 수를 변경하려 시도하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_failEmptyTable() {
        // given
        final OrderTable orderTable = new OrderTable(1L, 0, true, List.of(
            new Order(1L, OrderStatus.COOKING, List.of(new OrderLineItem(1L, 1L, "치킨", new MenuPrice(BigDecimal.TEN), null)))));

        // when
        // then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(5)).isInstanceOf(IllegalArgumentException.class);
    }
}
