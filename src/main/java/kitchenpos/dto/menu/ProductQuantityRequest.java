package kitchenpos.dto.menu;

import javax.validation.constraints.NotNull;

public class ProductQuantityRequest {

    @NotNull
    private Long productId;
    private long quantity;

    protected ProductQuantityRequest() {
    }

    public ProductQuantityRequest(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
