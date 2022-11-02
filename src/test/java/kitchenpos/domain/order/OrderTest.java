package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.ordertable.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("주문 생성자")
    @Nested
    class Constructor {
        @DisplayName("주문 항목이 비어있다면, IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderLineItemsIsEmpty() {
            // given
            OrderTable orderTable = new OrderTable(10, false);

            // when & then
            assertThatThrownBy(() -> new Order(orderTable, List.of()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 비어있다면, IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableIsEmpty() {
            // given
            OrderTable orderTable = new OrderTable(10, true);
            OrderLineItem orderLineItem = new OrderLineItem(1L, 10L);

            // when & then
            assertThatThrownBy(() -> new Order(orderTable, List.of(orderLineItem)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("상태 변경")
    @Nested
    class ChangeOrderStatus {
        @DisplayName("주문이 완료 상태라면, IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderStatusIsCompletion() {
            // given
            OrderLineItem orderLineItem = new OrderLineItem(1L, 1L);
            OrderTable orderTable = new OrderTable(10, false);
            Order order = new Order(1L, orderTable, OrderStatus.COMPLETION, LocalDateTime.now(),
                    List.of(orderLineItem));

            // when & then
            assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
