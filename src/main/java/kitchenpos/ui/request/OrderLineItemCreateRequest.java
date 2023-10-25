package kitchenpos.ui.request;

import javax.validation.constraints.NotNull;

public class OrderLineItemCreateRequest {

    @NotNull
    private Long menuId;
    @NotNull
    private Long quantity;

    public OrderLineItemCreateRequest() {

    }

    public OrderLineItemCreateRequest(Long menuId, Long quantity) {
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
