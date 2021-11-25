package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class OrderLineItemTest {

    @DisplayName("객체 생성: 객체가 정상적으로 생성된다.")
    @Test
    void create() {
        // given
        final Long seq = 1L;
        final Order order = new Order(2L, null, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
        final Menu menu = new Menu(3L, "피자", new BigDecimal(18000), 1L, null);
        final long quantity = 4L;

        // when
        OrderLineItem orderLineItem = new OrderLineItem(seq, order, menu.getId(), quantity);

        // then
        assertThat(orderLineItem.getSeq()).isEqualTo(1L);
        assertThat(orderLineItem.getOrder()).isEqualTo(new Order(2L, null, OrderStatus.COOKING.name(), LocalDateTime.now(), null));
        assertThat(orderLineItem.getMenuId()).isEqualTo(3L);
        assertThat(orderLineItem.getQuantity()).isEqualTo(4L);
    }

    @DisplayName("주문과 연결할 수 있다.")
    @Test
    void connectOrder() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem(1L, null, 2L, 2L);

        final Order order = new Order(1L, null, OrderStatus.COOKING.name(), LocalDateTime.now(), null);

        // when
        orderLineItem.connectOrder(order);

        // then
        assertThat(orderLineItem.getOrder()).isNotNull();
        assertThat(orderLineItem.getOrder()).isEqualTo(new Order(1L, null, OrderStatus.COOKING.name(), LocalDateTime.now(), null));
    }
}