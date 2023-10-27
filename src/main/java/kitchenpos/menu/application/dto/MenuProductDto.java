package kitchenpos.menu.application.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductDto {

    private final Long seq;
    private final Long productId;
    private final Long quantity;

    private MenuProductDto(Long id, Long productId, Long quantity) {
        this.seq = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductDto from(MenuProduct menuProduct) {
        return new MenuProductDto(
                menuProduct.getSeq(),
                menuProduct.getProduct().getId(),
                menuProduct.getQuantity()
        );
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
