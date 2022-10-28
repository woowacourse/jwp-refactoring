package kitchenpos.application.menu;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private final Long seq;
    private final Long productId;
    private final Long quantity;

    public MenuProductResponse(Long seq, Long productId, Long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getProduct().getId(),
                menuProduct.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
