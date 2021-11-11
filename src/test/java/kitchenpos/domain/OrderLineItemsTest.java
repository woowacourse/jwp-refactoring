package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
        final Menu menu = new Menu(1L, "피자", new BigDecimal(18000), new MenuGroup(1L, "피자"), null);
        final long quantity = 2L;
        OrderLineItem orderLineItem = new OrderLineItem(seq, order, menu, quantity);
        OrderLineItem orderLineItem2 = new OrderLineItem(seq, order, menu, quantity);

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
        final Menu menu = new Menu(1L, "피자", new BigDecimal(18000), new MenuGroup(1L, "피자"), null);
        final long quantity = 2L;
        OrderLineItem orderLineItem = new OrderLineItem(seq, order, menu, quantity);
        OrderLineItem orderLineItem2 = new OrderLineItem(seq, order, menu, quantity);
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
        final Menu menu = new Menu(1L, "피자", new BigDecimal(18000), new MenuGroup(1L, "피자"), null);
        final long quantity = 2L;
        OrderLineItem orderLineItem = new OrderLineItem(seq, order, menu, quantity);
        OrderLineItem orderLineItem2 = new OrderLineItem(seq, order, menu, quantity);
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(orderLineItem, orderLineItem2));

        // when
        List<Long> menuIds = orderLineItems.getMenuIds();

        // then
        assertThat(menuIds).hasSize(2);
        assertThat(menuIds).containsExactly(1L, 1L);
    }

    @DisplayName("connectOrder(): 모든 주문항목에 주문과 연결시켜준다.")
    @Test
    void connectOrder() {
        // given
        final Long seq = 1L;
        final Menu menu = new Menu(1L, "피자", new BigDecimal(18000), new MenuGroup(1L, "피자"), null);
        final long quantity = 2L;
        OrderLineItem orderLineItem = new OrderLineItem(seq, null, menu, quantity);
        OrderLineItem orderLineItem2 = new OrderLineItem(seq, null, menu, quantity);
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