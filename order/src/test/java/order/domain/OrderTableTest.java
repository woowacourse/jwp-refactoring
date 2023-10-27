package order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import common.domain.Price;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("주문 테이블을 빈 상태로 변경한다.")
    @Test
    void changeEmpty() {
        // given
        final OrderTable orderTable = new OrderTable(1L, 0, false);
        final List<Order> orders = List.of(
            new Order(1L, OrderStatus.COMPLETION,
                      List.of(new OrderLineItem(1L, 1L, "치킨", new Price(BigDecimal.TEN), null)), orderTable.getId()));

        // when
        orderTable.changeEmpty(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = new OrderTable(1L, 1, false);

        // when
        final int changedNumberOfGuests = 5;
        orderTable.changeNumberOfGuests(changedNumberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(changedNumberOfGuests);
    }
}
