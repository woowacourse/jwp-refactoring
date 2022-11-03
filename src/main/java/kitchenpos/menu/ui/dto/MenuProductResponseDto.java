package kitchenpos.menu.ui.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponseDto {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    private MenuProductResponseDto() {
    }

    public MenuProductResponseDto(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponseDto of(final MenuProduct menuProduct) {
        return new MenuProductResponseDto(menuProduct.getSeq(), menuProduct.getMenuId(), menuProduct.getProductId(),
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
