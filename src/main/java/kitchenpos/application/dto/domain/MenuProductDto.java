package kitchenpos.application.dto.domain;

import kitchenpos.domain.menu.MenuProduct;

public class MenuProductDto {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;


    public static MenuProductDto from(final MenuProduct menuProduct) {
        return new MenuProductDto(menuProduct.getSeq(), menuProduct.getMenu().getId(), menuProduct.getProductId(),
                menuProduct.getQuantity());
    }

    public MenuProductDto(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
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
