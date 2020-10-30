package kitchenpos.dto.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;

public class MenuProductRequest {
    private Long productId;
    private long quantity;

    private MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct to(Long menuId) {
        return new MenuProduct(new Menu(menuId), new Product(productId), quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
