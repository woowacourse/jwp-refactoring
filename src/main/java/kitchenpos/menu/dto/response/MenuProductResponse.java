package kitchenpos.menu.dto.response;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {

    private Long seq;
    private Long productId;
    private long quantity;

    public MenuProductResponse() {
    }

    private MenuProductResponse(Long seq, Long productId, long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        Long seq = menuProduct.getSeq();
        Long productId = menuProduct.getProduct().getId();
        long quantity = menuProduct.getQuantity();
        return new MenuProductResponse(seq, productId, quantity);
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
