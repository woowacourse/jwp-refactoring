package kitchenpos.domain;

import java.util.List;

public class OrderFixture {

    public static Order 조리중인_주문() {
        OrderTable orderTable = new OrderTable(0, false);
        OrderLineItem orderLineItem = new OrderLineItem(null, 0);
        return new Order(orderTable, OrderStatus.COOKING, new OrderLineItems(List.of(orderLineItem)));
    }

    public static Order 계산완료된_주문() {
        OrderTable orderTable = new OrderTable(0, false);
        OrderLineItem orderLineItem = new OrderLineItem(null, 0);
        return new Order(orderTable, OrderStatus.COMPLETION, new OrderLineItems(List.of(orderLineItem)));
    }
}
