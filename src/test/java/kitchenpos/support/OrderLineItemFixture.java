package kitchenpos.support;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderMenu;

public enum OrderLineItemFixture {

    ORDER_LINE_ITEM_1(1);

    private final int quantity;

    OrderLineItemFixture(int quantity) {
        this.quantity = quantity;
    }

    public OrderLineItem 생성(final OrderMenu orderMenu) {
        return new OrderLineItem(null, null, orderMenu, quantity);
    }
}
