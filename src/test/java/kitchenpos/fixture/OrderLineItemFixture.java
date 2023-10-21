package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem 주문_아이템(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        return new OrderLineItem(seq, orderId, menuId, quantity);
    }
}
