package kitchenpos.menu.ui.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductDto {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    private MenuProductDto() {
    }

    public MenuProductDto(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity() {
        return new MenuProduct(seq, menuId, productId, quantity);
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
