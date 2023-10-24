package kitchenpos.ui.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private final Long seq;
    private final Long productId;
    private final long quantity;

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(),
            menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }

    public MenuProductResponse(Long seq, Long productId, long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
