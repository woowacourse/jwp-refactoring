package kitchenpos.ui.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductRequest {

    private final Long productId;
    private final long quantity;

    public MenuProductRequest(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity() {
        return new MenuProduct(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
