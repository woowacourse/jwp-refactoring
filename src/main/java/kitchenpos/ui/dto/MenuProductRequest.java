package kitchenpos.ui.dto;

import javax.validation.constraints.NotNull;

public class MenuProductRequest {

    @NotNull
    private Long productId;
    private long quantity;

    public MenuProductRequest(Long productId, long quantity) {
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
