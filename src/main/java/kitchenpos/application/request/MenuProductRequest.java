package kitchenpos.application.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MenuProductRequest {

    private Long menuId;
    private Long productId;
    private long quantity;

    @JsonCreator
    public MenuProductRequest(final Long menuId, final Long productId, final long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
