package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("주문 순서를 변경한다.")
    @Test
    void changeOrderStatus() {
        OrderTable orderTable = new OrderTable(1L);
        Order order = new kitchenpos.domain.Order(1L, orderTable, OrderStatus.COOKING, LocalDateTime.now(), null);
        order.changeOrderStatus(OrderStatus.MEAL);

        assertThat(order.orderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상품을 변경한다.")
    @Test
    void changeOrderLineItems() {
        OrderTable orderTable = new OrderTable(1L);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(1L, null, null, 1L),
            new OrderLineItem(2L, null, null, 5L)
        );
        Order order = new kitchenpos.domain.Order(1L, orderTable, OrderStatus.COOKING, LocalDateTime.now(), null);
        Order expected = new kitchenpos.domain.Order(1L, orderTable, OrderStatus.COOKING, LocalDateTime.now(), Arrays.asList(
            new OrderLineItem(1L, null, null, 1L),
            new OrderLineItem(2L, null, null, 5L)
        ));
        order.changeOrderLineItems(orderLineItems);

        assertThat(order).usingRecursiveComparison().ignoringFields("orderedTime").isEqualTo(expected);
    }
}
