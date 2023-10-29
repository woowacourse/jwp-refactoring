package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.ordertable.domain.Order;
import kitchenpos.ordertable.domain.OrderLineItem;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Test
    void 중복_OrderLineItem_추기_시_예외() {
        // given
        Order order = new Order(1L, null, null, null, new ArrayList<>());
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, null, 10L);
        order.addOrderLineItems(List.of(orderLineItem1));

        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 1L, 5L);

        List<OrderLineItem> orderLineItems = List.of(orderLineItem2);

        // when && then
        assertThatThrownBy(() -> {
            order.addOrderLineItems(orderLineItems);
        })
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 다른_주문의_주문_상품을_넣으면_예외() {
        // given
        Order order = new Order(1L, null, null, null, new ArrayList<>());
        Order otherOrder = new Order(2L, null, null, null, new ArrayList<>());
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 2L, 10L);

        // when && then
        List<OrderLineItem> orderLineItems = List.of(orderLineItem);
        assertThatThrownBy(() -> order.addOrderLineItems(orderLineItems))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("다른 주문의 상품입니다.");
    }

}
