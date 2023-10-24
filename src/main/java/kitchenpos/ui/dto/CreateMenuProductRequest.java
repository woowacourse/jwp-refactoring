package kitchenpos.ui.dto;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class CreateMenuProductRequest {

    private Long productId;
    private long quantity;

    public CreateMenuProductRequest() {
    }

    public CreateMenuProductRequest(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct toEntity(final Product product) {
        return new MenuProduct(product, quantity);
    }
}
