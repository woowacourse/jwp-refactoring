package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductDto {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final Long quantity;

    private MenuProductDto(Long seq, Long menuId, Long productId, Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductDto from(final MenuProduct menuProduct){
        return new MenuProductDto(
                menuProduct.getSeq(),
                menuProduct.getMenuId(),
                menuProduct.getProduct(),
                menuProduct.getQuantity().getValue()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
