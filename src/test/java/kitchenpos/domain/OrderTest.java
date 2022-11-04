package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.common.vo.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Order 클래스의")
class OrderTest {

    @Nested
    @DisplayName("생성자는")
    class Constructor {

        @Test
        @DisplayName("OrderLineItem 리스트가 빈 리스트인 경우 예외를 던진다.")
        void orderLineItems_IsEmpty_ExceptionThrown() {
            assertThatThrownBy(() -> new Order(OrderStatus.COOKING, Collections.emptyList()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("updateOrderStatus 메서드는")
    class UpdateOrderStatus {

        @Test
        @DisplayName("order의 상태가 COMPLETION인 경우 예외를 던진다.")
        void orderStatus_IsCompleted_ExceptionThrown() {
            final OrderLineItem orderLineItem = new OrderLineItem(1L, "치킨", Price.valueOf(BigDecimal.TEN), 1);
            final Order order = new Order(OrderStatus.COMPLETION, List.of(orderLineItem));
            assertThatThrownBy(() -> order.updateOrderStatus(OrderStatus.COMPLETION.name()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
