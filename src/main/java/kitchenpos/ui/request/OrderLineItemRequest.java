package kitchenpos.ui.request;

import kitchenpos.menu.domain.Menu;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemRequest {

    private Long menuId;
    private long quantity;

    public static OrderLineItemRequest create(Long id, int quantity) {
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest();
        orderLineItemRequest.menuId = id;
        orderLineItemRequest.quantity = quantity;
        return orderLineItemRequest;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderLineItem toEntity() {
        final Menu menu = Menu.createSingleId(menuId);
        return OrderLineItem.create(menu, quantity);
    }
}
