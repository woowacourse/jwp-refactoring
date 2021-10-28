package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductDto {
    private Long seq;
    private Long productId;
    private long quantity;
    private Long menuId;

    public MenuProductDto() {
    }

    public MenuProductDto(MenuProduct menuProduct) {
        this.seq = menuProduct.getSeq();
        this.productId = menuProduct.getProductId();
        this.quantity = menuProduct.getQuantity();
        this.menuId = menuProduct.getMenuId();
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

    public Long getMenuId() {
        return menuId;
    }
}
