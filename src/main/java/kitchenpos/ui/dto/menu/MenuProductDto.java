package kitchenpos.ui.dto.menu;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductDto {

    private final Long productId;
    private final Long quantity;

    public MenuProductDto(final Long productId, final Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity(final Product product) {
        return new MenuProduct(product, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
