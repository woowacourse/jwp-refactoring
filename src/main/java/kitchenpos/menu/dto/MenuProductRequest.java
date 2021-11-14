package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductRequest {

    private final Long seq;
    private final Long productId;
    private final Long quantity;

    public MenuProductRequest(Long seq, Long productId, Long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity() {
        return new MenuProduct(seq, productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getSeq() {
        return seq;
    }
}
