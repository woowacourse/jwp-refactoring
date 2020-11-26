package kitchenpos.menu.dto;

import javax.validation.constraints.NotNull;

public class MenuProductCreateRequest {
    @NotNull
    private Long productId;
    @NotNull
    private Long quantity;

    public MenuProductCreateRequest() {
    }

    public MenuProductCreateRequest(Long productId, Long quantity) {
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
