package kitchenpos.support;

import kitchenpos.domain.OrderLineItem;

public enum OrderLineItemFixture {

    ORDER_LINE_ITEM_1(1);

    private final int quantity;

    OrderLineItemFixture(int quantity) {
        this.quantity = quantity;
    }

    public OrderLineItem 생성(final long menuId) {
        return new OrderLineItem(null, menuId, quantity);
    }

    public OrderLineItem 생성(final long orderId, final long menuId) {
        return new OrderLineItem(orderId, menuId, quantity);
    }
}
