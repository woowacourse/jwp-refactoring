package kitchenpos.order.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderLineItemRequest {

    private final long menuId;
    private final long quantity;

    @JsonCreator
    public OrderLineItemRequest(final long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
