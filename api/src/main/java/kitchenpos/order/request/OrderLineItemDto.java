package kitchenpos.order.request;

import javax.validation.constraints.NotNull;

public class OrderLineItemDto {

    @NotNull
    private final Long menuId;
    @NotNull
    private final long quantity;

    public OrderLineItemDto(Long menuId, long quantity) {
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
