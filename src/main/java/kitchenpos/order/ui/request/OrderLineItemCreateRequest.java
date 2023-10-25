package kitchenpos.order.ui.request;

import javax.validation.constraints.NotNull;

public class OrderLineItemCreateRequest {

    @NotNull
    private final Long menuId;

    @NotNull
    private final long quantity;

    public OrderLineItemCreateRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
