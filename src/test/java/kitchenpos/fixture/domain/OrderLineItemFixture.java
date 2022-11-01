package kitchenpos.fixture.domain;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem createOrderLineItem(Long menuId, long quantity) {
        return OrderLineItem.builder()
                .menuId(menuId)
                .quantity(quantity)
                .build();
    }
}
