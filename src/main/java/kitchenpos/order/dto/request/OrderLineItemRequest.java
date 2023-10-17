package kitchenpos.order.dto.request;

import com.sun.istack.NotNull;

public class OrderLineItemRequest {

    @NotNull
    private Long menuId;
    @NotNull
    private Long quantity;

    public OrderLineItemRequest(Long menuId, Long quantity) {
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
