package kitchenpos.application.dto.request;

import com.sun.istack.NotNull;

public class MenuProductRequest {
    @NotNull
    private Long productId;

    @NotNull
    private long quantity;

    private MenuProductRequest() {
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
