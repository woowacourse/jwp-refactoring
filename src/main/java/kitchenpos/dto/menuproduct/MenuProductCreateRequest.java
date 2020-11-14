package kitchenpos.dto.menuproduct;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.product.Product;

import javax.validation.constraints.NotNull;

public class MenuProductCreateRequest {
    @NotNull
    private Long productId;

    @NotNull
    private long quantity;

    public MenuProductCreateRequest() {
    }

    public MenuProductCreateRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toMenuProduct(Product product) {
        return new MenuProduct(product, this.quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
