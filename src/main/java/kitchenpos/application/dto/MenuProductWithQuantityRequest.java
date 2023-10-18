package kitchenpos.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MenuProductWithQuantityRequest {

    private final Long productId;
    private final Long quantity;

    @JsonCreator
    public MenuProductWithQuantityRequest(final Long productId, final Long quantity) {
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
