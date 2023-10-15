package kitchenpos.application.request;

import kitchenpos.domain.MenuProduct;

public class MenuProductCreateRequest {

    private final Long productId;
    private final long quantity;

    public MenuProductCreateRequest(Long productId, long quantity) {
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
