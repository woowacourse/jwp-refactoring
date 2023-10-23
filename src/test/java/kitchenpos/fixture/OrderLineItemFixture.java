package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem 주문상품(Menu menu, long quantity) {
        return new OrderLineItem(menu, quantity);
    }

//    public static OrderLineItem 주문상품(Order order, Menu menu, long quantity) {
//        OrderLineItem orderLineItem = new OrderLineItem(menu, quantity);
//        orderLineItem.setOrder(order);
//        return orderLineItem;
//    }
}
