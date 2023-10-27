package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem 주문_아이템(final Long seq, final Order order, final Menu menu, final long quantity) {
        return new OrderLineItem(seq, order, menu, quantity);
    }
}
