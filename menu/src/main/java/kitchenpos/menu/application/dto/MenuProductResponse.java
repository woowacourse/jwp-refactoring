package kitchenpos.menu.application.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {


    private Long seq;
    private Long productId;
    private long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(Long seq, Long productId, long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(),
                menuProduct.getProductId().getId(),
                menuProduct.getQuantity()
        );
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
