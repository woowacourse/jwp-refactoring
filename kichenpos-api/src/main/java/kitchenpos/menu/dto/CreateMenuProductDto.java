package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class CreateMenuProductDto {

    private final Long productId;
    private final long quantity;

    public CreateMenuProductDto(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct toMenuProduct() {
        return new MenuProduct(productId, quantity);
    }
}
