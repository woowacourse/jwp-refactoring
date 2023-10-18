package kitchenpos.test.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem 주문_메뉴_목록(Order order, Menu menu, long quantity) {
        return new OrderLineItem(order, menu, quantity);
    }
}
