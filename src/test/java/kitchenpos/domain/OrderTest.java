package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTest {

    @Test
    void 주문_생성() {
        final OrderTable orderTable = new OrderTable(null, 10, true);
        assertDoesNotThrow(
                () -> new Order(orderTable,
                        OrderStatus.COOKING.name(),
                        LocalDateTime.now(),
                        null)
        );
    }

    @Test
    void 주문_상태_변경() {
        final OrderTable orderTable = new OrderTable(null, 10, true);
        final Order order = new Order(orderTable,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                null);

        order.updateOrderStatus(OrderStatus.COMPLETION);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    void COMPLETION인_주문_상태_변경_예외_발생() {
        final OrderTable orderTable = new OrderTable(null, 10, true);
        final Order order = new Order(orderTable,
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                null);

        assertThatThrownBy(
                () -> order.updateOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문의 상태가 COMPLETION일 때는 상태 변경이 불가 합니다.");
    }

    @Test
    void 주문_상품_변경() {
        final OrderTable orderTable = new OrderTable(null, 10, true);
        final Order order = new Order(orderTable,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                null);

        final OrderLineItem orderLineItem = new OrderLineItem();
        order.updateOrderLineItems(List.of(orderLineItem));

        assertThat(order.getOrderLineItems()).contains(orderLineItem);
    }
}
