package kitchenpos.menu.application.response;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MenuProductResponse {

    private Long menuId;
    private Long productId;
    private long quantity;

    @JsonCreator
    public MenuProductResponse(final Long menuId, final Long productId, final long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }
}
