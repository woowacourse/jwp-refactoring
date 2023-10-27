package kitchenpos.domain.order;

import java.util.List;

public class OrderFixture {

    public static Order 조리중인_주문() {
        OrderLineItem orderLineItem = new OrderLineItem(null, 0);
        return new Order(1L, OrderStatus.COOKING, new OrderLineItems(List.of(orderLineItem)));
    }

    public static Order 계산완료된_주문() {
        OrderLineItem orderLineItem = new OrderLineItem(null, 0);
        return new Order(1L, OrderStatus.COMPLETION, new OrderLineItems(List.of(orderLineItem)));
    }
}
