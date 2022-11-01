package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTest {
    @ParameterizedTest
    @DisplayName("주문 완료 상태가 아닐 때의 주문 완료 여부를 반환한다")
    @ValueSource(strings = {"COOKING", "MEAL"})
    void isCompletionOrder_false(final String orderStatus) {
        final OrderLineItems orderLineItems = new OrderLineItems(new ArrayList<>());
        final Order order = new Order(1L, orderStatus, LocalDateTime.now(), orderLineItems);

        final boolean actual = order.isCompletionOrder();

        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("주문 완료 상태일 때의 주문 완료 여부를 반환한다")
    void isCompletionOrder_true() {
        final OrderLineItems orderLineItems = new OrderLineItems(new ArrayList<>());
        final Order order = new Order(1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems);

        final boolean actual = order.isCompletionOrder();

        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    void changeOrderStatus() {
        final String expected = OrderStatus.COMPLETION.name();
        final OrderLineItems orderLineItems = new OrderLineItems(new ArrayList<>());
        final Order order = new Order(1L, OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItems);

        order.changeOrderStatus(OrderStatus.COMPLETION.name());

        assertThat(order.getOrderStatus()).isEqualTo(expected);
    }
}
