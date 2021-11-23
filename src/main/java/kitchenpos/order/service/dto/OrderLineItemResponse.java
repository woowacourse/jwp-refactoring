package kitchenpos.order.service.dto;

import kitchenpos.menu.service.dto.MenuResponse;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private MenuResponse menuResponse;
    private long quantity;

    public OrderLineItemResponse(MenuResponse menuResponse, long quantity) {
        this.menuResponse = menuResponse;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem item) {
        return new OrderLineItemResponse(
            MenuResponse.of(item.getMenu()), item.getQuantity()
        );
    }

    public MenuResponse getMenuResponse() {
        return menuResponse;
    }

    public long getQuantity() {
        return quantity;
    }
}
