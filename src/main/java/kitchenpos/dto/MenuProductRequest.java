package kitchenpos.dto;

import javax.validation.constraints.NotNull;

public class MenuProductRequest {

    @NotNull
    private Long productId;

    @NotNull
    private Long quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, Long quantity) {
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
