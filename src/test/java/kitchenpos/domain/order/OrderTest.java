package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTest {

    @Mock
    private OrderValidator orderValidator;

    @DisplayName("주문 생성자")
    @Nested
    class Constructor {
        @DisplayName("주문 항목이 비어있다면, IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderLineItemsIsEmpty() {
            // given & when & then
            assertThatThrownBy(() -> new Order(1L, List.of(), orderValidator))
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
            OrderedMenu orderedMenu = new OrderedMenu(1L, "메뉴", BigDecimal.valueOf(10_000));
            OrderLineItem orderLineItem = new OrderLineItem(orderedMenu, 1L);
            Order order = new Order(1L, List.of(orderLineItem), orderValidator);
            order.changeOrderStatus(OrderStatus.COMPLETION);

            // when & then
            assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
