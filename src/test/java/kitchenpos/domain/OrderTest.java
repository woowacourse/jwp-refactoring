package kitchenpos.domain;

import static kitchenpos.fixture.OrderTableFactory.emptyTable;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("OrderLineItem 목록은 빈 목록이 될 수 없다")
    @Test
    void orderLineItemsIsEmpty_throwsException() {
        final List<OrderLineItem> invalidOrderLineItems = Collections.emptyList();

        assertThatThrownBy(
                () -> new Order(null, 1L, OrderStatus.COOKING, LocalDateTime.now(), invalidOrderLineItems)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상태가 COMPLETION이면, 상태를 변경할 수 없다")
    @Test
    void orderStatusIsCompletion_changeStatus_throwsException() {
        final var order = new Order(1L, 1L, OrderStatus.COMPLETION, LocalDateTime.now());

        assertThatThrownBy(
                () -> order.changeStatus(OrderStatus.COOKING)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 주문 테이블로 생성할 수 없다")
    @Test
    void create_orderTableIsEmptyTrue_throwsException() {
        final var table = emptyTable(2);

        assertThatThrownBy(
                () -> new Order(table, List.of(new OrderLineItem(null, null, 1)))
        );
    }
}
