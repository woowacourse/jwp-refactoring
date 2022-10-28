package kitchenpos.application.dto.request;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductCreateRequest {
    private final Long productId;
    private final long quantity;

    private MenuProductCreateRequest() {
        this(null, 0L);
    }

    public MenuProductCreateRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toMenuProduct(Product product) {
        return new MenuProduct(product, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
