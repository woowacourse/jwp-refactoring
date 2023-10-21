package kitchenpos.ui.request;

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

    public void setProductId(final Long productId) {
        this.productId = productId;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
