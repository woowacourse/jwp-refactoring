package kitchenpos.fixture;

import kitchenpos.application.dto.OrderLineItemDto;

public class OrderFixture {

    public static OrderLineItemDto createOrderLineItem(
        final Long menuId,
        final Long quantity
    ) {
        return new OrderLineItemDto(
            null,
            null,
            menuId,
            quantity
        );
    }
}
