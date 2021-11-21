package kitchenpos.application.dto.request.menu;

import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;

public class MenuProductRequest {

    private Long productId;
    private long quantity;

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct toEntity(Product product) {
        return new MenuProduct.MenuProductBuilder()
                .setProduct(product)
                .setQuantity(this.quantity)
                .build();
    }
}
