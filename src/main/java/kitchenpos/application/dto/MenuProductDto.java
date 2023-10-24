package kitchenpos.application.dto;

import kitchenpos.domain.menu.MenuProduct;

public class MenuProductDto {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final Long quantity;

    private MenuProductDto(Long id, Long menuId, Long productId, Long quantity) {
        this.seq = id;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductDto from(MenuProduct menuProduct) {
        return new MenuProductDto(
                menuProduct.getSeq(),
                menuProduct.getMenu().getId(),
                menuProduct.getProduct().getId(),
                menuProduct.getQuantity()
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
