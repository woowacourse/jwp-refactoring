package kitchenpos.dto;

import com.sun.istack.NotNull;

public class MenuProductRequest {
    @NotNull
    private Long productId;

    @NotNull
    private long quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(final Long productId, final long quantity) {
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
