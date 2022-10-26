package kitchenpos.application.dto.request;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductCreateRequest {
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    private MenuProductCreateRequest() {
        this(null, null, 0L);
    }

    public MenuProductCreateRequest(Long menuId, Long productId, long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toMenuProduct(Product product) {
        return new MenuProduct(menuId, product, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
