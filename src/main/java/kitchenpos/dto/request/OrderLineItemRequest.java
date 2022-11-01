package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;

public class OrderLineItemRequest {
    @NotNull
    private Long menuId;

    @NotNull
    private int quantity;

    private OrderLineItemRequest() {
    }

    public OrderLineItemRequest(final Long menuId, final int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }
}
