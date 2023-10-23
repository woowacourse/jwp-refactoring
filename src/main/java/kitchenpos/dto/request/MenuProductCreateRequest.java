package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;

public class MenuProductCreateRequest {

    @NotNull
    private final Long productId;

    @NotNull
    private final Long quantity;

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
