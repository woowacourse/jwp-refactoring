package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductDto {

    private Long seq;
    private Long productId;
    private Long quantity;

    public MenuProductDto() {
    }

    public MenuProductDto(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductDto(MenuProduct menuProduct) {
        this.seq = menuProduct.getSeq();
        this.productId = menuProduct.getProduct().getId();
        this.quantity = menuProduct.getQuantity();
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
