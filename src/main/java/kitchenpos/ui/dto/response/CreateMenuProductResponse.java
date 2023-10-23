package kitchenpos.ui.dto.response;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class CreateMenuProductResponse {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    public CreateMenuProductResponse(final Menu menu, final MenuProduct menuProduct) {
        this.seq = menuProduct.getSeq();
        this.menuId = menu.getId();
        this.productId = menuProduct.getProduct().getId();
        this.quantity = menuProduct.getQuantity();
    }

    public Long getSeq() {
        return seq;
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
