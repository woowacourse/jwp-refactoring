package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderMenus;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Test
    void 주문_상태를_변경한다() {
        // given
        final Order order = new Order();
        order.setOrderLineItems(List.of());
        order.setOrderStatus(OrderStatus.MEAL);

        // when
        final Order actual = order.updateOrderStatus(OrderStatus.COMPLETION);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    void 주문_상태를_변경할때_주문_상태가_COMPLETION_이면_예외가_발생한다() {
        // given
        final Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION);

        // when, then
        assertThatThrownBy(() -> order.updateOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_초기화한다() {
        // given
        final Order order = new Order();
        final OrderLineItem orderLineItem = new OrderLineItem();
        final Long menuId = 1L;
        orderLineItem.setMenuId(menuId);
        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(1L);
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        final OrderMenu orderMenu = new OrderMenu(1L, "메뉴이름", BigDecimal.valueOf(100), "메뉴그룹");
        orderMenu.setMenuId(menuId);
        final OrderMenus orderMenus = new OrderMenus(List.of(orderMenu));

        // when
        final Order actual = order.init(orderTable, orderMenus);

        // then
        final Order expected = new Order(null, order.getOrderTableId(), OrderStatus.COOKING, LocalDateTime.now(),
                order.getOrderLineItems());
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("orderedTime")
                .isEqualTo(expected);
    }
}
