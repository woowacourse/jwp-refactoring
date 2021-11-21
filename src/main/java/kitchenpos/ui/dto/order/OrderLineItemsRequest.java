package kitchenpos.ui.dto.order;

import javax.validation.constraints.NotNull;

public class OrderLineItemsRequest {

    @NotNull
    private Long menuId;
    private long quantity;

    public OrderLineItemsRequest(Long menuId, long quantity) {
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
