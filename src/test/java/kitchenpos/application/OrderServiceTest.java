package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTestContext {

    @Test
    void 주문_항목이_없다면_예외를_던진다() {
        // given
        Order order = new Order();
        order.setOrderLineItems(List.of());

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목의_메뉴가_존재하지_않는다면_예외를_던진다() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(Long.MAX_VALUE);
        orderLineItem.setQuantity(1L);

        Order order = new Order();
        order.setOrderLineItems(List.of(orderLineItem));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_존재하지_않는다면_예외를_던진다() {
        // given
        Order order = new Order();
        order.setOrderLineItems(List.of(savedOrderLineItem));
        order.setOrderTableId(Long.MAX_VALUE);

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문은_생성되면_COOKING_상태로_설정된다() {
        // given
        Order order = new Order();
        order.setOrderLineItems(List.of(savedOrderLineItem));
        order.setOrderTableId(savedOrderTable.getId());

        // when
        Order createdOrder = orderService.create(order);

        // then
        assertThat(createdOrder.getOrderStatus()).isEqualTo("COOKING");
    }

    @Test
    void 주문을_정상적으로_생성하는_경우_생성한_메뉴가_반환된다() {
        // given
        Order order = new Order();
        order.setOrderLineItems(List.of(savedOrderLineItem));
        order.setOrderTableId(savedOrderTable.getId());

        // when
        Order createdOrder = orderService.create(order);

        // then
        assertThat(createdOrder.getId()).isNotNull();
    }
}