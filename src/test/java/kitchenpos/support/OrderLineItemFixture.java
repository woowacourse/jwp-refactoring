package kitchenpos.support;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public enum OrderLineItemFixture {

    ORDER_LINE_ITEM_1(1);

    private final int quantity;

    OrderLineItemFixture(int quantity) {
        this.quantity = quantity;
    }

    public OrderLineItem 생성(final Menu menu) {
        return new OrderLineItem(null, null, menu, quantity);
    }

    public OrderLineItem 생성(final Order order, final Menu menu) {
        return new OrderLineItem(null, order, menu, quantity);
    }
}
