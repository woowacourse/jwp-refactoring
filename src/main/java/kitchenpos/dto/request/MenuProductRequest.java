package kitchenpos.dto.request;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;

public class MenuProductRequest {

    private Long productId;
    private long quantity;

    public MenuProductRequest(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity(final Menu menu) {
        return MenuProduct.ofNew(menu, productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    private MenuProductRequest() {
    }
}
