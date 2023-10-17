package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductCreateDto {

    private final Long productId;
    private final long quantity;

    public MenuProductCreateDto(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toDomain() {
        return new MenuProduct(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
