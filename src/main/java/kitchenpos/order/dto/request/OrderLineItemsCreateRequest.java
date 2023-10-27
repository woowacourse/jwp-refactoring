package kitchenpos.order.dto.request;

import javax.validation.constraints.NotNull;

public class OrderLineItemsCreateRequest {

    @NotNull
    private final Long menuId;

    @NotNull
    private final Long quantity;

    public OrderLineItemsCreateRequest(final Long menuId, final Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
