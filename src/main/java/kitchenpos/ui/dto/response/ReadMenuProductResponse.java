package kitchenpos.ui.dto.response;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;

public class ReadMenuProductResponse {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    public ReadMenuProductResponse(final Menu menu, final MenuProduct menuProduct) {
        this.seq = menuProduct.getSeq();
        this.menuId = menu.getId();
        this.productId = menuProduct.getProductId();
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
