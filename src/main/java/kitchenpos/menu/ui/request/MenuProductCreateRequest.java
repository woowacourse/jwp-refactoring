package kitchenpos.menu.ui.request;

import javax.validation.constraints.NotNull;

public class MenuProductCreateRequest {

    @NotNull
    private Long productId;

    @NotNull
    private Long quantity;

    public MenuProductCreateRequest(final Long productId, final Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
