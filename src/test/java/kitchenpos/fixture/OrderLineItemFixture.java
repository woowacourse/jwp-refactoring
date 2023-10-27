package kitchenpos.fixture;

import kitchenpos.menu.Menu;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem 주문_아이템(final Long seq, final Order order, final Menu menu, final long quantity) {
        return new OrderLineItem(seq, order, menu, quantity);
    }
}
