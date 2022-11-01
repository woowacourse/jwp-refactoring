package kitchenpos.order.application.request;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemCommand {

    private Long menuId;
    private long quantity;

    private OrderLineItemCommand() {
    }

    public OrderLineItemCommand(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(menuId, quantity);
    }
}
