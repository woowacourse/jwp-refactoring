package kitchenpos.dto.menu;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductCreateRequest {

    private Long productId;
    private long quantity;

    public MenuProductCreateRequest() {
    }

    public MenuProductCreateRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toMenuProduct(Product product) {
        return MenuProduct.of(product, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
