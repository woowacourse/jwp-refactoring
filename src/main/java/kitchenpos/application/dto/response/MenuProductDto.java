package kitchenpos.application.dto.response;

import kitchenpos.domain.MenuProduct;

public class MenuProductDto {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final Integer quantity;

    public MenuProductDto(Long seq, Long menuId, Long productId, Integer quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductDto of(MenuProduct menuProduct) {
        return new MenuProductDto(
                menuProduct.getSeq(),
                menuProduct.getMenuId(),
                menuProduct.getProductId(),
                menuProduct.getQuantity());
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

    public Integer getQuantity() {
        return quantity;
    }
}
