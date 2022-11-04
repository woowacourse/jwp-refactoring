package kitchenpos.order.ui.response;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderLineItemResponse {

    private final long menuId;
    private final long quantity;

    @JsonCreator
    public OrderLineItemResponse(final long menuId, final long quantity) {
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
