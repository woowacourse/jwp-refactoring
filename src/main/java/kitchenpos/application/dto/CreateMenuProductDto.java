package kitchenpos.application.dto;

import kitchenpos.domain.menu.MenuProduct;

public class CreateMenuProductDto {

    private Long seq;
    private Long productId;
    private long quantity;

    public CreateMenuProductDto(final MenuProduct menuProduct) {
        this.seq = menuProduct.getSeq();
        this.productId = menuProduct.getProductId();
        this.quantity = menuProduct.getQuantity();
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
