package kitchenpos.dto.order;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemRequest {
    private Long menuId;
    private long quantity;

    private OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem to() {
        return new OrderLineItem(new Menu(menuId), quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
