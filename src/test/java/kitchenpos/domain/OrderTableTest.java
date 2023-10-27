package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
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
