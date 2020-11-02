package kitchenpos.ui.dto;

import javax.validation.constraints.NotNull;

public class OrderLineItemRequest {

    @NotNull
    private Long menuId;

    @NotNull
    private Long quantity;

    private OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderLineItemRequest{" +
            "menuId=" + menuId +
            ", quantity=" + quantity +
            '}';
    }
}
