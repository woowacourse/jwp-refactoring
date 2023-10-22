package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductDto {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    public MenuProductDto(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductDto toDto(final MenuProduct menuProduct) {
        return new MenuProductDto(menuProduct.getSeq(), menuProduct.getMenu().getId(), menuProduct.getProduct().getId(),
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
