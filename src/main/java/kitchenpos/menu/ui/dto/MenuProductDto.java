package kitchenpos.menu.ui.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductDto {

    private final Long productId;
    private final Long quantity;

    public MenuProductDto(final Long productId, final Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity() {
        return new MenuProduct(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
