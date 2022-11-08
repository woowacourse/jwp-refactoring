package kitchenpos.domain;

import static kitchenpos.order.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    private static final long ORDER_TABLE_ID = 1L;
    private static final long QUANTITY = 1L;

    @DisplayName("주문을 할 때 하나 이상의 메뉴를 주문해야한다.")
    @Test
    void createOrderWithOneMenu() {
        assertThatThrownBy(() -> Order.of(ORDER_TABLE_ID, LocalDateTime.now(), List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태가 이미 계산 완료이면 주문 상태를 변경할 수 없다.")
    @Test
    void canNotChangeOrderStatusWhenAlreadyCompletion() {
        // given
        final Menu menu = new Menu("메뉴 이름", BigDecimal.ONE, 1L, new MenuProducts(List.of()));
        final OrderLineItem orderLineItem = new OrderLineItem(menu, QUANTITY);
        final Order order = Order.of(1L, LocalDateTime.now(), List.of(orderLineItem));
        order.changeOrderStatus(COMPLETION);

        // when & then
        assertThatThrownBy(() -> order.changeOrderStatus(COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
