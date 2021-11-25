package kitchenpos.order.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderLineItemsTest {

    @DisplayName("객체 생성: 객체가 정상적으로 생성된다.")
    @Test
    void create() {
        // given
        final Long seq = 1L;
        final Order order = new Order(1L, null, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
        final Long menuId = 2L;
        final long quantity = 2L;
        OrderLineItem orderLineItem = new OrderLineItem(seq, order, menuId, quantity);
        OrderLineItem orderLineItem2 = new OrderLineItem(seq, order, menuId, quantity);

        // when
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(orderLineItem, orderLineItem2));

        // then
        assertThat(orderLineItems.getValues()).hasSize(2);
    }

    @DisplayName("isDifferentSize(): 해당 사이즈와 다른지 여부를 반환한다.")
    @Test
    void isDifferentSize() {
        // given
        final Long seq = 1L;
        final Order order = new Order(1L, null, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
        final Long menuId = 2L;
        final long quantity = 2L;
        OrderLineItem orderLineItem = new OrderLineItem(seq, order, menuId, quantity);
        OrderLineItem orderLineItem2 = new OrderLineItem(seq, order, menuId, quantity);
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(orderLineItem, orderLineItem2));

        // when
        boolean result = orderLineItems.isDifferentSize(2);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("getMenuIds(): 주문 항목들의 메뉴 id들을 반환한다.")
    @Test
    void getMenuIds() {
        // given
        final Long seq = 1L;
        final Order order = new Order(1L, null, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
        final Long menuId = 2L;
        final long quantity = 2L;
        OrderLineItem orderLineItem = new OrderLineItem(seq, order, menuId, quantity);
        OrderLineItem orderLineItem2 = new OrderLineItem(seq, order, menuId, quantity);
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(orderLineItem, orderLineItem2));

        // when
        List<Long> menuIds = orderLineItems.getMenuIds();

        // then
        assertThat(menuIds).hasSize(2);
        assertThat(menuIds).containsExactly(2L, 2L);
    }

    @DisplayName("connectOrder(): 모든 주문항목에 주문과 연결시켜준다.")
    @Test
    void connectOrder() {
        // given
        final Long seq = 1L;
        final Long menuId = 2L;
        final long quantity = 2L;
        OrderLineItem orderLineItem = new OrderLineItem(seq, null, menuId, quantity);
        OrderLineItem orderLineItem2 = new OrderLineItem(seq, null, menuId, quantity);
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(orderLineItem, orderLineItem2));
        final Order order = new Order(1L, null, OrderStatus.COOKING.name(), LocalDateTime.now(), null);

        // when
        orderLineItems.connectOrder(order);

        // then
        assertThat(orderLineItems.getValues())
                .extracting("order")
                .containsExactly(order, order);
    }
}