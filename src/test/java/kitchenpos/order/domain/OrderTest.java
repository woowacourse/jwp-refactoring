package kitchenpos.order.domain;

import static kitchenpos.fixture.MenProductFixture.메뉴_상품_1000원_2개;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTest {

    private final Menu menu = Menu.of("메뉴", BigDecimal.valueOf(1_000L), null, List.of(메뉴_상품_1000원_2개));
    private final OrderLineItem orderLineItem1 = new OrderLineItem(null, menu.getId(), 1L);
    private final OrderLineItem orderLineItem2 = new OrderLineItem(null, menu.getId(), 1L);
    private final List<OrderLineItem> orderLineItems = List.of(orderLineItem1, orderLineItem2);

    @Test
    void 주문_생성() {
        final OrderTable orderTable = new OrderTable(null, 10, false);
        assertDoesNotThrow(
                () -> new Order(orderTable, LocalDateTime.now(), orderLineItems)
        );
    }

    @Test
    void 주문_상태_변경() {
        final OrderTable orderTable = new OrderTable(null, 10, false);
        final Order order = new Order(orderTable, LocalDateTime.now(), orderLineItems);

        order.updateOrderStatus("COMPLETION");

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    void COMPLETION인_주문_상태_변경_예외_발생() {
        final OrderTable orderTable = new OrderTable(null, 10, false);
        final Order order = new Order(orderTable, LocalDateTime.now(), orderLineItems);
        order.updateOrderStatus("COMPLETION");

        assertThatThrownBy(
                () -> order.updateOrderStatus("COOKING"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문의 상태가 COMPLETION일 때는 상태 변경이 불가 합니다.");
    }

    @Test
    void 주문_상품_변경() {
        final OrderTable orderTable = new OrderTable(null, 10, false);
        final Order order = new Order(orderTable, LocalDateTime.now(), orderLineItems);

        final OrderLineItem orderLineItem = new OrderLineItem();
        order.updateOrderLineItems(List.of(orderLineItem));

        assertThat(order.getOrderLineItems()).contains(orderLineItem);
    }
}
