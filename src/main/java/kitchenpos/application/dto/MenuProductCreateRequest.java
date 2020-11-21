package kitchenpos.application.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductCreateRequest {
    private Long productId;
    private long quantity;

    private MenuProductCreateRequest() {
    }

    public MenuProductCreateRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity() {
        return new MenuProduct(null, null, productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
