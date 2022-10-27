package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductCreateRequest {

    private Long productId;
    private long quantity;

    private MenuProductCreateRequest() {
    }

    public MenuProductCreateRequest(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct toEntity() {
        return new MenuProduct(productId, quantity);
    }
}
