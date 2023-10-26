package kitchenpos.menu.application.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductResponse {

    private Long productId;
    private long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        final Product product = menuProduct.getProduct();
        return new MenuProductResponse(product.getId(), menuProduct.getQuantity());
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
