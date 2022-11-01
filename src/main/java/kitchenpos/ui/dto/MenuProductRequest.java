package kitchenpos.ui.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductRequest {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductRequest(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    private MenuProductRequest() {
    }

    public MenuProduct toMenuProduct() {
        return new MenuProduct(seq, menuId, productId, quantity);
    }
}
