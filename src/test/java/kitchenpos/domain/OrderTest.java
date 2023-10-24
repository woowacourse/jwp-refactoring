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
        final OrderTable orderTable = new OrderTable(null, 10, false);
        assertDoesNotThrow(
                () -> new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now())
        );
    }

    @Test
    void 주문_상태_변경() {
        final OrderTable orderTable = new OrderTable(null, 10, false);
        final Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());

        order.updateOrderStatus("COMPLETION");

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    void COMPLETION인_주문_상태_변경_예외_발생() {
        final OrderTable orderTable = new OrderTable(null, 10, false);
        final Order order = new Order(orderTable, OrderStatus.COMPLETION, LocalDateTime.now());

        assertThatThrownBy(
                () -> order.updateOrderStatus("COOKING"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문의 상태가 COMPLETION일 때는 상태 변경이 불가 합니다.");
    }

    @Test
    void 주문_상품_변경() {
        final OrderTable orderTable = new OrderTable(null, 10, false);
        final Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());

        final OrderLineItem orderLineItem = new OrderLineItem();
        order.updateOrderLineItems(List.of(orderLineItem));

        assertThat(order.getOrderLineItems()).contains(orderLineItem);
    }
}
