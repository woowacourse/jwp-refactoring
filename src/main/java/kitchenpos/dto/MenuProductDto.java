package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductDto {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public static MenuProductDto from(MenuProduct menuProduct) {
        return new MenuProductDto(menuProduct.getSeq(), menuProduct.getProductId(),
                menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public MenuProductDto(Long productId, long quantity) {
        this(null, null, productId, quantity);
    }

    public MenuProductDto(Long seq, Long menuId, Long productId, long quantity) {
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
