package kitchenpos.ui.dto.response;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private final Long seq;
    private final long quantity;
    private final Long productId;

    public MenuProductResponse(final Long seq, final long quantity, final Long productId) {
        this.seq = seq;
        this.quantity = quantity;
        this.productId = productId;
    }

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getQuantity(), menuProduct.getProduct().getId());
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }
}
