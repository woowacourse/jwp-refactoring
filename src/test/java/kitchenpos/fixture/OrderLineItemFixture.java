package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem 주문_아이템(final Long seq, final Order order, final Menu menu, final long quantity) {
        return new OrderLineItem(seq, order, menu, quantity);
    }
}
