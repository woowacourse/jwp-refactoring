package kitchenpos.dto;

import javax.validation.constraints.NotNull;

public class OrderMenuRequest {

    @NotNull
    private Long menuId;

    @NotNull
    private Long quantity;

    public OrderMenuRequest() {
    }

    public OrderMenuRequest(Long menuId, Long quantity) {
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
