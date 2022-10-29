package kitchenpos.ui.request.menu;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductDto {

    private Long productId;
    private long quantity;

    public MenuProductDto() {
    }

    public MenuProductDto(Long productId, long quantity) {
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
        return new MenuProduct(product.getId(), quantity, product.getPrice());
    }
}
