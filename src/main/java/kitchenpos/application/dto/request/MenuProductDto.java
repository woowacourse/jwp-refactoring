package kitchenpos.application.dto.request;

import kitchenpos.domain.MenuProduct;

public class MenuProductDto {

    private Long productId;
    private long quantity;

    public MenuProductDto() {
    }

    public MenuProductDto(final MenuProduct menuProduct) {
        this.productId = menuProduct.getProductId();
        this.quantity = menuProduct.getQuantity();
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct toMenuProduct() {
        return new MenuProduct(null, this.productId, this.quantity);
    }
}
