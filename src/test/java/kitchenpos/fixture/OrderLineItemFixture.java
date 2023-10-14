package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem 주문_항목(Long menuId, long quantity) {
        return new OrderLineItem(menuId, quantity);
    }
}
