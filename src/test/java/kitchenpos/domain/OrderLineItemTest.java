package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderLineItemTest {

    @DisplayName("객체 생성: 객체가 정상적으로 생성된다.")
    @Test
    void create() {
        // given
        final Long seq = 1L;
        final Order order = new Order(1L, null, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
        final Menu menu = new Menu(1L, "피자", new BigDecimal(18000), new MenuGroup(1L, "피자"), null);
        final long quantity = 2L;

        // when
        OrderLineItem orderLineItem = new OrderLineItem(seq, order, menu, quantity);

        // then
        assertThat(orderLineItem.getSeq()).isEqualTo(seq);
        assertThat(orderLineItem.getOrder()).isEqualTo(order);
        assertThat(orderLineItem.getMenu()).isEqualTo(menu);
        assertThat(orderLineItem.getQuantity()).isEqualTo(quantity);
    }

    @DisplayName("주문과 연결할 수 있다.")
    @Test
    void connectOrder() {
        // given
        final Menu menu = new Menu(1L, "피자", new BigDecimal(18000), new MenuGroup(1L, "피자"), null);
        final long quantity = 2L;
        OrderLineItem orderLineItem = new OrderLineItem(1L, null, menu, quantity);

        final Order order = new Order(1L, null, OrderStatus.COOKING.name(), LocalDateTime.now(), null);

        // when
        orderLineItem.connectOrder(order);

        // then
        assertThat(orderLineItem.getOrder()).isNotNull();
        assertThat(orderLineItem.getOrder()).isEqualTo(order);
    }
}