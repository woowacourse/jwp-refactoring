package kitchenpos.application.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {

    private MenuResponse menu;
    private long quantity;

    private OrderLineItemResponse(final MenuResponse menu, final long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                MenuResponse.of(orderLineItem.getMenu()), orderLineItem.getQuantity()
        );
    }

    public MenuResponse getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
