package kitchenpos.ui.dto.menu;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private Long id;
    private Long seq;
    private Long menuId;
    private Long productId;
    private Long quantity;

    public MenuProductResponse(Long id, Long seq, Long menuId, Long productId, Long quantity) {
        this.id = id;
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getId(),
                menuProduct.getSeq(),
                menuProduct.getMenu().getId(),
                menuProduct.getProduct().getId(),
                menuProduct.getQuantity()
        );
    }

    public Long getId() {
        return id;
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
