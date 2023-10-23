package kitchenpos.dto.menu;

import kitchenpos.domain.MenuProduct;

public class MenuProductDto {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductDto(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductDto from(final MenuProduct menuProduct) {
        return new MenuProductDto(menuProduct.getSeq(),
            menuProduct.getMenu().getId(),
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

    public long getQuantity() {
        return quantity;
    }
}
